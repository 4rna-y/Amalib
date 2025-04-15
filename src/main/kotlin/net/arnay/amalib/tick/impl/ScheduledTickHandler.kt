package net.arnay.amalib.tick.impl

import net.arnay.amalib.tick.TickScheduler

class ScheduledTickHandler(private val tickScheduler: TickScheduler) : Runnable
{
    override fun run()
    {
        tickScheduler.tick()
        tickScheduler.tickCount++
    }
}