package net.arnay.amalib.event.impl

import net.arnay.amalib.dependency.ServiceProvider
import net.arnay.amalib.event.EventRegistry
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

class EventRegistryImpl(
    private val plugin: JavaPlugin
) : EventRegistry
{
    override fun <TEventListener : Listener> register(clazz: KClass<TEventListener>, serviceProvider: ServiceProvider)
    {
        val ctor = clazz.constructors.firstOrNull() ?:
        throw IllegalArgumentException("${clazz.simpleName} doesn't have any constructor.")
        val args = ctor.parameters.associateWith {
            val dependency = it.type.classifier as? KClass<*> ?:
            throw IllegalArgumentException("${it.name} was invalid.")
            serviceProvider.getRequiredService(dependency)
        }

        val instance = ctor.callBy(args)
        plugin.server.pluginManager.registerEvents(instance, plugin)
    }
}