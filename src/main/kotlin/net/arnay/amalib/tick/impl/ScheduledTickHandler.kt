package net.arnay.amalib.tick.impl

import net.arnay.amalib.tick.TickScheduler

class ScheduledTickHandler(
    val tickScheduler: TickScheduler,
    private val canceller: TickCanceller
) : Runnable
{
    var id: Int = -1

    override fun run() {
        if (tickScheduler.tickCount == 0) tickScheduler.onStart()

        when
        {
            tickScheduler.isTerminated ->
            {
                tickScheduler.onTerminate()
                canceller.removeScheduler(this)
                return
            }
            tickScheduler.tickCount >= tickScheduler.maxTime &&
            tickScheduler.maxTime != TickScheduler.INFINITE_MAX_TIME ->
            {
                tickScheduler.cancelled = true
            }
        }

        if (tickScheduler.cancelled)
        {
            tickScheduler.onComplete()
            canceller.removeScheduler(this)
            return
        }

        if (tickScheduler.isPausing) return

        tickScheduler.onTick()
        tickScheduler.tickCount++
    }

    fun terminate()
    {
        tickScheduler.pause()
        tickScheduler.onTerminate()
    }
}