package net.arnay.amalib.command

import net.arnay.amalib.command.impl.StructuredCommandObject
import net.arnay.amalib.command.node.ArgumentBranchNode
import net.arnay.amalib.dependency.ServiceProvider

@Suppress("unused")
class StructuredCommandBuilder(
    name: String,
    serviceProvider: ServiceProvider
) : StructuredCommandBuilderBase<CommandObject>(
    name,
    serviceProvider,
    StructuredCommandOption()
)
{

    override fun build(): CommandObject
    {
        val value = StructuredCommandObject(name, nodes, option)
        return value
    }

    fun configure(predicate: () -> StructuredCommandOption) : StructuredCommandBuilderBase<CommandObject>
    {
        option = predicate()
        return this
    }

    class BranchBuilder(
        name: String,
        serviceProvider: ServiceProvider,
        option: StructuredCommandOption
    ) : StructuredCommandBuilderBase<ArgumentBranchNode>(
        name,
        serviceProvider,
        option)
    {
        override fun build(): ArgumentBranchNode
        {
            val value = ArgumentBranchNode(name, option)
            value.setNodes(nodes)
            return value
        }
    }
}