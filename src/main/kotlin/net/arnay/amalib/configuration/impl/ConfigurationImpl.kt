package net.arnay.amalib.configuration.impl

import net.arnay.amalib.configuration.ConfigEntry
import net.arnay.amalib.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import java.lang.reflect.Array as JArray

class ConfigurationImpl<T: Any>(private val plugin: JavaPlugin, private val clazz: KClass<T>) : Configuration<T>
{
    private var configObj: T

    init
    {
        plugin.saveDefaultConfig()
        configObj = load(clazz)
    }

    override fun get() : T
    {
        return configObj
    }

    override fun set(supplier: (T) -> Unit)
    {
        supplier(configObj)
        save()
    }

    override fun reload()
    {
        configObj = load(clazz)
    }

    private fun save()
    {
        val clazz = configObj::class
        val config = plugin.config
        for (prop in clazz.memberProperties)
        {
            val anno = prop.findAnnotation<ConfigEntry>() ?: continue
            val value = prop.getter.call(configObj)
            config.set(anno.propertyName, value)
        }

        plugin.saveConfig()
    }

    private fun load(clazz: KClass<T>): T
    {
        plugin.reloadConfig()
        val config = plugin.config
        require(clazz.isData) { "${clazz.simpleName} is not data class" }
        val ctor = clazz.primaryConstructor ?: clazz.constructors.first()
        ctor.isAccessible = true
        val args = ctor.parameters.associateWith { p ->
            val key = p.findAnnotation<ConfigEntry>()?.propertyName ?: p.name
            ?: error("Unnamed parameter in ${clazz.simpleName}")
            val raw = config.get(key)
            parse(raw, p.type)
        }
        return ctor.callBy(args)
    }

    @Suppress("UNCHECKED_CAST")
    private fun parse(value: Any?, kType: KType): Any?
    {
        if (value == null) {
            require(kType.isMarkedNullable) { "Non-nullable $kType cannot be null" }
            return null
        }

        val nonNullType = kType.withNullability(false)
        val clazz = nonNullType.classifier as KClass<*>

        when (clazz) {
            String::class -> return value.toString()
            Char::class -> return when (value) {
                is Char -> value
                is Number -> value.toInt().toChar()
                else -> value.toString().single()
            }
            Boolean::class -> return when (value) {
                is Boolean -> value
                is String -> value.equals("true", true) || value == "1" || value.equals("yes", true) || value.equals("on", true)
                is Number -> value.toInt() != 0
                else -> false
            }
            Byte::class, Short::class, Int::class, Long::class, Float::class, Double::class -> {
                val num = when (value) {
                    is Number -> value
                    is String -> value.toDouble()
                    else -> error("Cannot cast $value to Number")
                }
                return when (clazz) {
                    Byte::class -> num.toByte()
                    Short::class -> num.toShort()
                    Int::class -> num.toInt()
                    Long::class -> num.toLong()
                    Float::class -> num.toFloat()
                    Double::class -> num.toDouble()
                    else -> error("Unexpected numeric type")
                }
            }
        }

        if (clazz.java.isEnum) {
            val name = value.toString()
            return java.lang.Enum.valueOf(clazz.java as Class<out Enum<*>>, name)
        }

        if (clazz.java.isArray) {
            val elemType = clazz.java.componentType.kotlin.createType()
            val srcList = when (value) {
                is Collection<*> -> value.toList()
                is Array<*> -> value.toList()
                else -> error("Cannot cast $value to array")
            }
            val parsed = srcList.map { parse(it, elemType) }.toTypedArray()
            val javaArr = JArray.newInstance(clazz.java.componentType, parsed.size)
            parsed.forEachIndexed { i, v -> JArray.set(javaArr, i, v) }
            return javaArr
        }

        if (clazz == List::class || clazz == Set::class) {
            require(nonNullType.arguments.size == 1)
            val elemType = nonNullType.arguments[0].type ?: error("Unknown element type for $nonNullType")
            val src: Iterable<*> = when (value) {
                is Iterable<*> -> value
                is Array<*> -> value.asIterable()
                is String -> value.split(",")
                else -> error("Cannot cast $value to iterable")
            }
            val parsed = src.map { parse(it, elemType) }
            return if (clazz == Set::class) parsed.toSet() else parsed
        }

        if (clazz == Map::class) {
            require(nonNullType.arguments.size == 2)
            val keyType = nonNullType.arguments[0].type ?: error("Unknown key type for $nonNullType")
            val valueType = nonNullType.arguments[1].type ?: error("Unknown value type for $nonNullType")
            val src: Map<*, *> = when (value) {
                is Map<*, *> -> value
                is ConfigurationSection -> value.getValues(false)
                else -> error("Cannot cast $value to map")
            }
            return src.entries.associate { (k, v) -> parse(k, keyType) to parse(v, valueType) }
        }

        if (clazz.isData) {
            val mapSrc: Map<*, *> = when (value) {
                is Map<*, *> -> value
                is ConfigurationSection -> value.getValues(false)
                else -> error("Cannot cast $value to map for data class")
            }
            val ctor = clazz.primaryConstructor ?: clazz.constructors.first()
            ctor.isAccessible = true
            val args = ctor.parameters.associateWith { p ->
                val key = p.findAnnotation<ConfigEntry>()?.propertyName ?: p.name
                ?: error("Unnamed parameter in $clazz")
                val raw = mapSrc[key]
                parse(raw, p.type)
            }
            return ctor.callBy(args)
        }

        if (clazz.isInstance(value)) return value
        error("Unsupported type conversion: $value to $kType")
    }
}
