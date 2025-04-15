package net.arnay.amalib

import net.arnay.amalib.command.StructuredCommandRegistry
import net.arnay.amalib.tick.TickSchedulerRegistry
import org.bukkit.plugin.java.JavaPlugin

class Amalib
{
    companion object
    {
        fun initialize(plugin: JavaPlugin)
        {
            plugin.slF4JLogger.info("Initializing...")

            StructuredCommandRegistry.setPlugin(plugin)
            TickSchedulerRegistry.setScheduler(plugin)
        }
    }
}
