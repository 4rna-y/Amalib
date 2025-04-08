package net.arnay.amalib.command

import net.arnay.amalib.command.impl.StructuredCommandObject
import net.arnay.amalib.command.node.ArgumentBranchNode

class StructuredCommandBuilder(name: String) : StructuredCommandBuilderBase<CommandObject>(name)
{
    override fun build(): CommandObject
    {
        val value = StructuredCommandObject(name, nodes)
        return value
    }

    class BranchBuilder(name: String) : StructuredCommandBuilderBase<ArgumentBranchNode>(name)
    {
        override fun build(): ArgumentBranchNode
        {
            val value = ArgumentBranchNode(name)
            value.setNodes(nodes)
            return value
        }
    }

}