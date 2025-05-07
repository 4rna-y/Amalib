package net.arnay.amalib.dependency

import net.arnay.amalib.command.StructuredCommandRegistry
import net.arnay.amalib.command.impl.StructuredCommandRegistryImpl
import net.arnay.amalib.configuration.Configuration
import net.arnay.amalib.configuration.impl.ConfigurationImpl
import net.arnay.amalib.dependency.impl.ServiceCollectionProvider
import net.arnay.amalib.event.EventRegistry
import net.arnay.amalib.event.impl.EventRegistryImpl
import net.arnay.amalib.shared.Builder
import net.arnay.amalib.tick.TickSchedulerRegistry
import net.arnay.amalib.tick.impl.TickSchedulerRegistryImpl
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import kotlin.reflect.KClass

@Suppress("unused", "MemberVisibilityCanBePrivate")
class ServiceCollection(val plugin: JavaPlugin) : Builder<ServiceProvider>
{
    val singletons = mutableMapOf<KClass<*>, Any>(JavaPlugin::class to plugin)
    val transients = mutableMapOf<KClass<*>, KClass<*>>()

    override fun build(): ServiceProvider
    {
        return ServiceCollectionProvider(singletons, transients)
    }

    fun useStructuredCommand() : ServiceCollection
    {
        return addSingleton<StructuredCommandRegistry, StructuredCommandRegistryImpl>()
    }

    fun useTickScheduler() : ServiceCollection
    {
        return addSingleton<TickSchedulerRegistry, TickSchedulerRegistryImpl>()
    }

    fun useEvent(): ServiceCollection
    {
        return addSingleton<EventRegistry, EventRegistryImpl>()
    }

    inline fun <reified T: Any> useConfiguration() : ServiceCollection
    {
        return addSingleton<Configuration<T>, ConfigurationImpl<T>>
        {
            ConfigurationImpl(plugin, T::class)
        }
    }

    inline fun <reified TInterface: Any, reified TImplementation: TInterface> addSingleton() : ServiceCollection
    {
        if (singletons.containsKey(TInterface::class) || transients.containsKey(TInterface::class))
            throw IllegalArgumentException("Class ${TInterface::class} was already registered")

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

    fun addLogger(logger: Logger): ServiceCollection
    {
        singletons[Logger::class] = logger
        return this
    }
}