package net.arnay.amalib.tick.impl

interface TickCanceller
{
    fun removeScheduler(tickHandler: ScheduledTickHandler)
}