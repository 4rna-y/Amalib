package net.arnay.amalib.command

import net.arnay.amalib.command.node.ArgumentNode
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter

interface CommandObject
{
    val name: String
    val nodes: MutableList<ArgumentNode>

    fun getExecutor(): CommandExecutor
    fun getCompleter(): TabCompleter
}