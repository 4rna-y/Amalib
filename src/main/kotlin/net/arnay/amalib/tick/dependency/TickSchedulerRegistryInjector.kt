package net.arnay.amalib.tick.dependency

import net.arnay.amalib.dependency.ServiceCollection
import net.arnay.amalib.tick.TickSchedulerRegistry
import net.arnay.amalib.tick.impl.TickSchedulerRegistryImpl

fun ServiceCollection.useTickScheduler() : ServiceCollection
{
    return addSingleton<TickSchedulerRegistry, TickSchedulerRegistryImpl>()
}