package net.arnay.amalib.event.dependency

import net.arnay.amalib.dependency.ServiceCollection
import net.arnay.amalib.event.EventRegistry
import net.arnay.amalib.event.impl.EventRegistryImpl

fun ServiceCollection.useEvent(): ServiceCollection
{
    return addSingleton<EventRegistry, EventRegistryImpl>()
}