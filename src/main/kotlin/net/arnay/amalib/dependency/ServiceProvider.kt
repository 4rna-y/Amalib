package net.arnay.amalib.dependency

import kotlin.reflect.KClass

interface ServiceProvider
{
    fun <TInterface: Any> getRequiredService(clazz: KClass<TInterface>): TInterface
}

inline fun <reified TInterface: Any> ServiceProvider.getRequiredService(): TInterface
{
    return getRequiredService(TInterface::class)
}