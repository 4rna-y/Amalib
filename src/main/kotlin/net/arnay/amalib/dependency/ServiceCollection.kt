package net.arnay.amalib.dependency

import net.arnay.amalib.dependency.impl.ServiceCollectionProvider
import net.arnay.amalib.shared.Builder
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import kotlin.reflect.KClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ServiceCollection(val plugin: JavaPlugin) : Builder<ServiceProvider>
{
    val singletonEntries = mutableMapOf<KClass<*>, KClass<*>>()
    val singletons       = mutableMapOf<KClass<*>, Any>(JavaPlugin::class to plugin)
    val transientEntries = mutableMapOf<KClass<*>, KClass<*>>()

    private val pending = mutableSetOf<KClass<*>>()

    override fun build(): ServiceProvider
    {
        for ((key, _) in singletonEntries)
        {
            createSingletonObject(key)
        }

        return ServiceCollectionProvider(singletons, transientEntries)
    }

    private fun createSingletonObject(clazz: KClass<*>) : Any
    {
        if (!pending.add(clazz)) error("Circular dependency detected at ${clazz.simpleName}")
        try
        {
            val value = singletonEntries[clazz] ?: error("${clazz.simpleName} was not found")
            val ctor  = value.constructors.firstOrNull() ?: error("Primary constructor of Class ${value.simpleName} was not found")
            val args  = ctor.parameters.associateWith {
                val paramClass = it.type.classifier as? KClass<*> ?: error("${it.name} was invalid")
                if (transientEntries.containsKey(paramClass)) createTransientObject(paramClass)
                else singletons[paramClass] ?: createSingletonObject(paramClass)
            }
            val instance = ctor.callBy(args)
            singletons[clazz] = instance
            return instance
        }
        finally
        {
            pending.remove(clazz)
        }
    }

    private fun createTransientObject(clazz: KClass<*>) : Any
    {
        if (!pending.add(clazz)) error("Circular dependency detected at ${clazz.simpleName}")
        try
        {
            val value = transientEntries[clazz] ?: error("${clazz.simpleName} was not found")
            val ctor  = value.constructors.firstOrNull() ?: error("Primary constructor of Class ${value.simpleName} was not found")
            val args  = ctor.parameters.associateWith {
                val paramClass = it.type.classifier as? KClass<*> ?: error("${it.name} was invalid")
                if (singletonEntries.containsKey(paramClass)) error("Do not inject singleton object to transient object")
                transientEntries[paramClass] ?: createTransientObject(paramClass)
            }
            return ctor.callBy(args)
        }
        finally
        {
            pending.remove(clazz)
        }
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addSingleton() : ServiceCollection
    {
        singletonEntries[TInterface::class] = TImplementation::class
        return this
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addSingleton(implInstance: () -> TImplementation) : ServiceCollection
    {
        singletons[TInterface::class] = implInstance()
        return this
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addTransient() : ServiceCollection
    {
        transientEntries[TInterface::class] = TImplementation::class
        return this
    }

    fun addLogger(logger: Logger): ServiceCollection
    {
        singletons[Logger::class] = logger
        return this
    }
}
