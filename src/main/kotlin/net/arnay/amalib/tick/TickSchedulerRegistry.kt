package net.arnay.amalib.tick

import net.arnay.amalib.tick.impl.ScheduledTickHandler
import net.arnay.amalib.tick.impl.SchedulerCollection
import net.arnay.amalib.tick.impl.TickCancellationHandler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

object TickSchedulerRegistry
{
    private lateinit var plugin: JavaPlugin
    private val schedulers = SchedulerCollection()

    fun setScheduler(plugin: JavaPlugin)
    {
        this.plugin = plugin
        this.plugin.server.scheduler.scheduleSyncRepeatingTask(
            this.plugin,
            TickCancellationHandler(this.plugin.server.scheduler, schedulers),
            0,
            1)
    }

    fun register(tickScheduler: TickScheduler) : Boolean
    {
        val id = this.plugin.server.scheduler.scheduleSyncRepeatingTask(
            this.plugin,
            ScheduledTickHandler(tickScheduler),
            0,
            tickScheduler.period
        )

        if (id == -1) return false

        schedulers.schedulers[id] = tickScheduler

        return true
    }

}