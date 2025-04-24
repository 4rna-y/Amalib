package net.arnay.amalib.command.node

import net.arnay.amalib.command.StructuredCommandOption
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

class ArgumentBranchNode(
    name: String,
    private val option: StructuredCommandOption
) : ArgumentNode(name)
{
    private var nodes = mutableListOf<ArgumentNode>()

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean
    {
        if (args.isEmpty())
        {
            sender.sendMessage(
                Component.text(option.argumentInvalidMessage, option.argumentInvalidColor))
            return false
        }

        val node = nodes.find { it.name == args[0] }
        if (node == null)
        {
            sender.sendMessage(
                Component.text(option.argumentInvalidMessage, option.argumentInvalidColor))
            return false
        }

        return node.execute(sender, args.drop(1).toTypedArray())
    }

    fun getNodes(): MutableList<ArgumentNode> = nodes
    fun setNodes(nodes: MutableList<ArgumentNode>)
    {
        this.nodes = nodes
    }
}