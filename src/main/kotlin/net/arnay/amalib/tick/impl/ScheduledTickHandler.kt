package net.arnay.amalib.tick.impl

import net.arnay.amalib.tick.TickScheduler

class ScheduledTickHandler(private val tickScheduler: TickScheduler) : Runnable
{
    override fun run()
    {
        if (tickScheduler.tickCount == 0)
        {
            tickScheduler.start()
        }

        tickScheduler.tick()
        tickScheduler.tickCount++
    }
}