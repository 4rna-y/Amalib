package net.arnay.amalib

import net.arnay.amalib.command.StructuredCommandBuilder
import net.arnay.amalib.command.StructuredCommandRegistry
import org.bukkit.plugin.java.JavaPlugin

class Amalib : JavaPlugin()
{

    override fun onEnable()
    {
        StructuredCommandBuilder().add(
            StructuredCommandBuilder.BranchBuilder("aaa"))
    }

    override fun onDisable()
    {
        // Plugin shutdown logic
    }
}
