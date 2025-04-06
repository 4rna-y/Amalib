package net.arnay.amalib.command

import org.bukkit.command.CommandSender

class StructuredSubCommandTree(
    override val key: String,
    private vararg val subCommands: StructuredSubCommandEntry
) : StructuredSubCommandEntry
{
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean
    {
        if (args.isEmpty())
        {
            sender.sendMessage("no arguments")
            return false
        }

        val sub = subCommands.find { it.key == args[0] }
        if (sub == null)
        {
            return false
        }

        return sub.execute(sender, args.drop(1).toTypedArray())
    }

    override fun complete(args: Array<out String>): MutableList<String>
    {
        if (args.isEmpty()) return subCommands.map { it.key }.toMutableList()
        else
        {
            val sub = subCommands.find { it.key == args[0] } ?: return subCommands.map { it.key }.toMutableList()
            return sub.complete(args.drop(0).toTypedArray())
        }
    }
}