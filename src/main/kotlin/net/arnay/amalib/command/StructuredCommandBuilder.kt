package net.arnay.amalib.command

import net.arnay.amalib.command.impl.StructuredCommandObject
import net.arnay.amalib.command.node.ArgumentBranchNode
import net.arnay.amalib.command.node.ArgumentExecutorNode
import net.arnay.amalib.command.node.ArgumentNode
import net.arnay.amalib.shared.Builder

class StructuredCommandBuilder(private val name: String) : Builder<CommandObject>
{
    private val nodes = mutableListOf<ArgumentNode>()

    override fun build(): CommandObject
    {
        val value = StructuredCommandObject(name, nodes)
        return value
    }

    fun add(name: String, predicate: (BranchBuilder) -> Unit): StructuredCommandBuilder
    {
        val branch = BranchBuilder("name")
        predicate(branch)
        nodes.add(branch.build())
        return this
    }

    fun add(executorNode: ArgumentExecutorNode) : StructuredCommandBuilder
    {
        nodes.add(executorNode)
        return this
    }

    class BranchBuilder(private val name: String) : Builder<ArgumentBranchNode>
    {
        private val nodes = mutableListOf<ArgumentNode>()
        override fun build(): ArgumentBranchNode
        {
            val value = ArgumentBranchNode(name)
            value.setNodes(nodes)
            return value
        }

        fun add(name: String, predicate: (BranchBuilder) -> Unit): BranchBuilder
        {
            val branch = BranchBuilder("name")
            predicate(branch)
            nodes.add(branch.build())
            return this
        }

        fun add(executorNode: ArgumentExecutorNode) : BranchBuilder
        {
            nodes.add(executorNode)
            return this
        }
    }

}