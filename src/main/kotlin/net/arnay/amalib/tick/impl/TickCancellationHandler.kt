package net.arnay.amalib.tick.impl

import org.bukkit.scheduler.BukkitScheduler

class TickCancellationHandler(
    private val serverScheduler: BukkitScheduler,
    private val schedulers: SchedulerCollection
) : Runnable
{
    override fun run()
    {
        for (scheduler in schedulers.schedulers)
        {
            if (scheduler.value.cancelled)
            {
                schedulers.schedulers.remove(scheduler.key)
                serverScheduler.cancelTask(scheduler.key)
            }
        }
    }
}