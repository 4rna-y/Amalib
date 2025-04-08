package net.arnay.amalib.command

import net.arnay.amalib.command.node.ArgumentExecutorNode
import net.arnay.amalib.command.node.ArgumentNode
import net.arnay.amalib.shared.Builder

abstract class StructuredCommandBuilderBase<T>(internal val name: String) : Builder<T>
{
    internal val nodes = mutableListOf<ArgumentNode>()

    fun add(name: String, predicate: (StructuredCommandBuilder.BranchBuilder) -> Unit): StructuredCommandBuilderBase<T>
    {
        val branch = StructuredCommandBuilder.BranchBuilder(name)
        predicate(branch)
        nodes.add(branch.build())
        return this
    }

    fun add(executorNode: ArgumentExecutorNode) : StructuredCommandBuilderBase<T>
    {
        nodes.add(executorNode)
        return this
    }
}