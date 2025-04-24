package net.arnay.amalib.command.impl

import net.arnay.amalib.command.CommandObject
import net.arnay.amalib.command.StructuredCommandOption
import net.arnay.amalib.command.node.ArgumentNode
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter

class StructuredCommandObject(
    override val name: String,
    override val nodes: MutableList<ArgumentNode>,
    options: StructuredCommandOption) : CommandObject
{
    private val executor = StructuredCommandExecutor(nodes, options)
    private val completer = StructuredCommandCompleter(nodes)

    override fun getExecutor(): CommandExecutor = executor
    override fun getCompleter(): TabCompleter = completer
}