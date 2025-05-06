package net.arnay.amalib.tick.impl

import net.arnay.amalib.tick.TickScheduler
import net.arnay.amalib.tick.TickSchedulerRegistry
import org.bukkit.plugin.java.JavaPlugin

class TickSchedulerRegistryImpl(private val plugin: JavaPlugin) : TickSchedulerRegistry, TickCanceller
{
    private val schedulers = mutableListOf<ScheduledTickHandler>()

    override fun register(tickScheduler: TickScheduler) : Boolean
    {
        val internalScheduler = ScheduledTickHandler(tickScheduler, this)

        val id = this.plugin.server.scheduler.scheduleSyncRepeatingTask(
            this.plugin,
            internalScheduler,
            0,
            tickScheduler.period
        )

        internalScheduler.id = id

        schedulers.add(internalScheduler)

        return internalScheduler.id != -1
    }

    override fun terminate(tickScheduler: TickScheduler)
    {
        val sth = schedulers.firstOrNull { it.tickScheduler == tickScheduler } ?: return
        removeScheduler(sth)
    }

    private fun clear()
    {
        this.plugin.server.scheduler.cancelTasks(this.plugin)
        schedulers.clear()
    }

    override fun removeScheduler(tickHandler: ScheduledTickHandler)
    {
        tickHandler.terminate()
        this.plugin.server.scheduler.cancelTask(tickHandler.id)
        schedulers.remove(tickHandler)
    }

}