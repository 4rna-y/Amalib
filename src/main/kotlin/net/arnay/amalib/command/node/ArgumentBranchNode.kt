package net.arnay.amalib.command.node

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandSender

class ArgumentBranchNode(name: String) : ArgumentNode(name)
{
    private var nodes = mutableListOf<ArgumentNode>()

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean
    {
        if (args.isEmpty())
        {
            sender.sendMessage(Component.text("Invalid arguments", TextColor.color(255, 255, 0)))
            return false
        }

        val node = nodes.find { it.name == args[0] }
        if (node == null)
        {
            sender.sendMessage(Component.text("Invalid arguments", TextColor.color(255, 255, 0)))
            return false
        }

        return node.execute(sender, args)
    }
    fun getNodes(): MutableList<ArgumentNode> = nodes
    fun setNodes(nodes: MutableList<ArgumentNode>)
    {
        this.nodes = nodes
    }
}