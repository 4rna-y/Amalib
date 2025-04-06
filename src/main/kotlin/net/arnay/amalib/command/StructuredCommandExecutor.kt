package net.arnay.amalib.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class StructuredCommandExecutor(private val subCommands: Array<out StructuredSubCommandEntry>) : TabExecutor
{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>
    {
        if (args.isEmpty()) return subCommands.map { it.key }.toMutableList()
        else
        {
            val sub = subCommands.find { it.key == args[0] } ?: return subCommands.map { it.key }.toMutableList()
            return sub.complete(args.drop(1).toTypedArray())
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean
    {
        if (args.isEmpty())
        {
            return false
        }

        val sub = subCommands.find { it.key == args[0] }
        if (sub == null)
        {
            return false
        }

        return sub.execute(sender, args.drop(0).toTypedArray())
    }

}