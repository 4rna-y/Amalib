package net.arnay.amalib.dependency

import net.arnay.amalib.dependency.impl.ServiceCollectionProvider
import kotlin.reflect.KClass

class ServiceCollection
{
    val services = mutableMapOf<KClass<*>, Any>()

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> register() : ServiceCollection
    {
        val ctor = TImplementation::class.constructors.firstOrNull() ?:
            throw IllegalArgumentException("Class ${TImplementation::class.simpleName} was not found.")

        val args = ctor.parameters.associateWith {
            val dependency = it.type.classifier as? KClass<*> ?:
                throw IllegalArgumentException("${it.name} was invalid.")
            services[dependency] ?: throw IllegalArgumentException("${it.name} was not registered.")
        }

        val instance = ctor.callBy(args)
        services[TInterface::class] = instance

        return this
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> register(
        implInstance: () -> TImplementation
    ) : ServiceCollection
    {
        services[TInterface::class] = implInstance()

        return this
    }

    fun build(): ServiceProvider
    {
        return ServiceCollectionProvider(services)
    }

}