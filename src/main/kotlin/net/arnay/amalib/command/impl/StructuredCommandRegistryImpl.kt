package net.arnay.amalib.command.impl

import net.arnay.amalib.command.CommandObject
import net.arnay.amalib.command.StructuredCommandRegistry
import org.bukkit.plugin.java.JavaPlugin

class StructuredCommandRegistryImpl(private val plugin: JavaPlugin) : StructuredCommandRegistry
{
    override fun register(
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