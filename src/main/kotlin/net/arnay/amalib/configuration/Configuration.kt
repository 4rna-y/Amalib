package net.arnay.amalib.configuration

import kotlin.reflect.KClass

interface Configuration
{
    fun <T: Any> get(clazz: KClass<T>) : T
}

inline fun <reified T: Any> Configuration.get(): T = get(T::class)