package net.arnay.amalib.command.impl

import net.arnay.amalib.command.StructuredCommandOption
import net.arnay.amalib.command.node.ArgumentNode
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StructuredCommandExecutor(
    private val nodes: MutableList<ArgumentNode>,
    private val option: StructuredCommandOption) : CommandExecutor
{
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean
    {
        if (args.isEmpty())
        {
            return sendInvalidArgumentMessage(sender)
        }

        val node = nodes.find { it.name == args[0] }
        if (node == null)
        {
            return sendInvalidArgumentMessage(sender)
        }

        return node.execute(sender, args.drop(1).toTypedArray())
    }

    private fun sendInvalidArgumentMessage(sender: CommandSender): Boolean
    {
        sender.sendMessage(Component.text(option.argumentInvalidMessage, option.argumentInvalidColor))
        return false
    }
}