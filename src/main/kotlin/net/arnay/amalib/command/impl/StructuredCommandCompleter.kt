package net.arnay.amalib.command.impl

import net.arnay.amalib.command.node.ArgumentBranchNode
import net.arnay.amalib.command.node.ArgumentNode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class StructuredCommandCompleter(private val nodes: MutableList<ArgumentNode>) : TabCompleter
{
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>
    {
        var currentDepthNodes = nodes
        var idx = 0

        while (idx < args.size - 1)
        {
            val input = args[idx]
            val next = currentDepthNodes.find { it.name == input }

            if (next is ArgumentBranchNode) currentDepthNodes = next.getNodes()
            else return mutableListOf()

            idx++
        }

        val currentArg = args.getOrNull(idx) ?: ""
        return currentDepthNodes
            .map { it.name }
            .filter { it.startsWith(currentArg) }
            .toMutableList()
    }
}