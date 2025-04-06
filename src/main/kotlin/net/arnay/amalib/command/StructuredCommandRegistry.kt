package net.arnay.amalib.command

import org.bukkit.plugin.java.JavaPlugin

object StructuredCommandRegistry
{
    fun register(
        plugin: JavaPlugin,
        label: String,
        vararg subCommandEntries: StructuredSubCommandEntry
    )
    {
        val command = plugin.getCommand(label)
        if (command == null)
        {
            plugin.slF4JLogger.error("The command $label is not found.")
            return
        }



        plugin.slF4JLogger.info("The command $label was successfully registered.")
    }
}