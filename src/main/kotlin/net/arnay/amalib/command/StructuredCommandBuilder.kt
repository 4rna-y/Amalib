package net.arnay.amalib.command

import net.arnay.amalib.command.impl.StructuredCommandObject
import net.arnay.amalib.command.node.ArgumentBranchNode
import net.arnay.amalib.dependency.ServiceProvider

class StructuredCommandBuilder(
    name: String,
    serviceProvider: ServiceProvider
) : StructuredCommandBuilderBase<CommandObject>(name, serviceProvider)
{
    override fun build(): CommandObject
    {
        val value = StructuredCommandObject(name, nodes)
        return value
    }

    fun configure()

    class BranchBuilder(name: String, serviceProvider: ServiceProvider) : StructuredCommandBuilderBase<ArgumentBranchNode>(name, serviceProvider)
    {
        override fun build(): ArgumentBranchNode
        {
            val value = ArgumentBranchNode(name)
            value.setNodes(nodes)
            return value
        }
    }
}