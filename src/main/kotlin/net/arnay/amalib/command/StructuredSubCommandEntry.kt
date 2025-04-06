package net.arnay.amalib.command

import org.bukkit.command.CommandSender

interface StructuredSubCommandEntry
{
    val key: String

    fun execute(sender: CommandSender, args: Array<out String>): Boolean
    fun complete(args: Array<out String>): MutableList<String>
}