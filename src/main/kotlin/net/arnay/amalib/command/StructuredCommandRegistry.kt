package net.arnay.amalib.command

import org.bukkit.plugin.java.JavaPlugin

object StructuredCommandRegistry
{
    private lateinit var plugin: JavaPlugin

    fun setPlugin(plugin: JavaPlugin)
    {
        this.plugin = plugin
    }

    fun register(
        commandObject: CommandObject
    )
    {
        val command = plugin.getCommand(commandObject.name)
        if (command == null)
        {
            plugin.slF4JLogger.error("The command ${commandObject.name} is not found.")
            return
        }

        command.setExecutor(commandObject.getExecutor())
        command.tabCompleter = commandObject.getCompleter()

        plugin.slF4JLogger.info("The command ${commandObject.name} was successfully registered.")
    }
}