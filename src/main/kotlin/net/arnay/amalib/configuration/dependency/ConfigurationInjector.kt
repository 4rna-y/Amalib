package net.arnay.amalib.configuration.dependency

import net.arnay.amalib.configuration.Configuration
import net.arnay.amalib.configuration.impl.ConfigurationImpl
import net.arnay.amalib.dependency.ServiceCollection

inline fun <reified T: Any> ServiceCollection.useConfiguration() : ServiceCollection
{
    return addSingleton<Configuration<T>, ConfigurationImpl<T>>
    {
        ConfigurationImpl(plugin, T::class)
    }
}