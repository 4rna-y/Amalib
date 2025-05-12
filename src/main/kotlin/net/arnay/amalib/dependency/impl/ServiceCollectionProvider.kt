package net.arnay.amalib.dependency.impl

import net.arnay.amalib.command.StructuredCommandRegistry
import net.arnay.amalib.configuration.Configuration
import net.arnay.amalib.dependency.ServiceProvider
import net.arnay.amalib.event.EventRegistry
import net.arnay.amalib.tick.TickSchedulerRegistry
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ServiceCollectionProvider(
    private val singletons: MutableMap<KClass<*>, Any>,
    private val transients: MutableMap<KClass<*>, KClass<*>>
) : ServiceProvider
{
    override fun getStructuredCommandRegistry() : StructuredCommandRegistry
    {
        return getRequiredService(StructuredCommandRegistry::class)
    }

    override fun getTickSchedulerRegistry() : TickSchedulerRegistry
    {
        return getRequiredService(TickSchedulerRegistry::class)
    }

    override fun getEventRegistry(): EventRegistry
    {
        return getRequiredService(EventRegistry::class)
    }

    override fun getConfiguration(): Configuration<*>
    {
        return getRequiredService(Configuration::class)
    }

    override fun <TInterface: Any> getRequiredService(clazz: KClass<TInterface>): TInterface
    {
        val value = singletons[clazz] ?: createTransientObject(clazz)

        return clazz.cast(value)
    }

    private fun createTransientObject(clazz: KClass<*>) : Any
    {
        val value = transients[clazz] ?: error("${clazz.simpleName} was not found")
        val ctor  = value.constructors.firstOrNull() ?: error("Primary constructor of Class ${value.simpleName} was not found")
        val args  = ctor.parameters.associateWith {
            val paramClass = it.type.classifier as? KClass<*> ?: error("${it.name} was invalid")
            if (singletons.containsKey(paramClass)) error("Do not inject singleton object to transient object")
            transients[paramClass] ?: createTransientObject(paramClass)
        }
        return ctor.callBy(args)

    }

}