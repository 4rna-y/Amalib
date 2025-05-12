package net.arnay.amalib.event

import net.arnay.amalib.dependency.ServiceProvider
import org.bukkit.event.Listener
import kotlin.reflect.KClass

interface EventRegistry
{
    fun <TEventListener : Listener> register(clazz: KClass<TEventListener>, serviceProvider: ServiceProvider)
}

inline fun <reified TEventListener : Listener> EventRegistry.register(serviceProvider: ServiceProvider)
{
    register(TEventListener::class, serviceProvider)
}