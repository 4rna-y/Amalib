package net.arnay.amalib.dependency

import net.arnay.amalib.command.StructuredCommandRegistry
import net.arnay.amalib.configuration.Configuration
import net.arnay.amalib.event.EventRegistry
import net.arnay.amalib.tick.TickSchedulerRegistry
import kotlin.reflect.KClass

interface ServiceProvider
{
    fun getStructuredCommandRegistry() : StructuredCommandRegistry
    fun getTickSchedulerRegistry() : TickSchedulerRegistry
    fun getEventRegistry() : EventRegistry
    fun getConfiguration(): Configuration<*>
    fun <TInterface: Any> getRequiredService(clazz: KClass<TInterface>): TInterface
}

inline fun <reified TInterface: Any> ServiceProvider.getRequiredService(): TInterface
{
    return getRequiredService(TInterface::class)
}