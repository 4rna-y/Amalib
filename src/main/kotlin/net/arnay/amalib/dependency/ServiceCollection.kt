package net.arnay.amalib.dependency

import net.arnay.amalib.dependency.impl.ServiceCollectionProvider
import net.arnay.amalib.shared.Builder
import kotlin.reflect.KClass

@Suppress("unused")
class ServiceCollection : Builder<ServiceProvider>
{
    val singletons = mutableMapOf<KClass<*>, Any>()
    val transients = mutableMapOf<KClass<*>, KClass<*>>()

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addSingleton() : ServiceCollection
    {
        val ctor = TImplementation::class.constructors.firstOrNull() ?:
            throw IllegalArgumentException("Class ${TImplementation::class.simpleName} was not found.")

        val args = ctor.parameters.associateWith {
            val dependency = it.type.classifier as? KClass<*> ?:
                throw IllegalArgumentException("${it.name} was invalid.")
            var res = singletons[dependency]
            if (res == null)
               res = transients[dependency] ?: throw IllegalArgumentException("${it.name} was not registered.")

            res
        }

        val instance = ctor.callBy(args)
        singletons[TInterface::class] = instance

        return this
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addSingleton(
        implInstance: () -> TImplementation
    ) : ServiceCollection
    {
        singletons[TInterface::class] = implInstance()

        return this
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addTransient(): ServiceCollection
    {
        transients[TInterface::class] = TImplementation::class
        return this
    }


    override fun build(): ServiceProvider
    {
        return ServiceCollectionProvider(singletons, transients)
    }

}