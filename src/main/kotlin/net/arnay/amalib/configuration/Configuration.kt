package net.arnay.amalib.configuration

import kotlin.reflect.KClass

interface Configuration<T: Any>
{
    fun get() : T
    fun set(supplier: (T) -> Unit)
    fun reload()
}