package net.arnay.amalib.dependency.impl

import net.arnay.amalib.dependency.ServiceProvider
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ServiceCollectionProvider(
    private val singletons: MutableMap<KClass<*>, Any>,
    private val transients: MutableMap<KClass<*>, KClass<*>>
) : ServiceProvider
{
    override fun <TInterface: Any> getRequiredService(clazz: KClass<TInterface>): TInterface
    {
        val value = singletons[clazz] ?: createTransientObject(clazz)

        return clazz.cast(value)
    }

    private fun createTransientObject(clazz: KClass<*>): Any
    {
        val transientType = transients[clazz] ?:
        throw IllegalArgumentException("Class ${clazz.simpleName} was not registered.")
        val ctor = transientType.constructors.firstOrNull() ?:
        throw IllegalArgumentException("Class ${clazz.simpleName} was not found.")
        val args = ctor.parameters.associateWith {
            val dependency = it.type.classifier as? KClass<*> ?:
            throw IllegalArgumentException("${it.name} was invalid.")
            var res = singletons[dependency]
            if (res == null)
                res = transients[dependency] ?: throw IllegalArgumentException("${it.name} was not registered.")

            res
        }

        return ctor.callBy(args)
    }

}