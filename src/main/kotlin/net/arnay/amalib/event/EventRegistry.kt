package net.arnay.amalib.event

import net.arnay.amalib.dependency.ServiceProvider
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

interface EventRegistry
{
    fun <TEventListener : Listener> register(clazz: KClass<TEventListener>, plugin: JavaPlugin, serviceProvider: ServiceProvider)
}

inline fun <reified TEventListener : Listener> EventRegistry.register(plugin: JavaPlugin, serviceProvider: ServiceProvider)
{
    register(TEventListener::class, plugin, serviceProvider)
}