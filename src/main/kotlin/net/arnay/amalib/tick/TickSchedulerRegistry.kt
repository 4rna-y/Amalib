package net.arnay.amalib.tick

interface TickSchedulerRegistry
{
    fun register(tickScheduler: TickScheduler) : Boolean
    fun terminate(tickScheduler: TickScheduler)
}