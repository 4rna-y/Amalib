package net.arnay.amalib.dependency.impl

import net.arnay.amalib.dependency.ServiceProvider
import org.apache.commons.lang3.ClassUtils.Interfaces
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ServiceCollectionProvider(
    private val services: MutableMap<KClass<*>, Any>
) : ServiceProvider
{
    override fun <TInterface: Any> getRequiredService(clazz: KClass<TInterface>): TInterface
    {
        val value = services[clazz] ?:
            throw IllegalArgumentException("Class ${clazz.simpleName} was not registered.")

        return clazz.cast(value)
    }

}