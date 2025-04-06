package net.arnay.amalib.command.node

import org.bukkit.command.CommandSender

abstract class ArgumentNode(val name: String)
{
    abstract fun execute(sender: CommandSender, args: Array<out String>): Boolean
}