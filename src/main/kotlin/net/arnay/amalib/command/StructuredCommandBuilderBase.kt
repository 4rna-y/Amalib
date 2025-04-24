package net.arnay.amalib.command

import net.arnay.amalib.command.node.ArgumentExecutorNode
import net.arnay.amalib.command.node.ArgumentNode
import net.arnay.amalib.dependency.ServiceProvider
import net.arnay.amalib.shared.Builder
import kotlin.reflect.KClass

abstract class StructuredCommandBuilderBase<T>(
    internal val name: String,
    val serviceProvider: ServiceProvider,
    internal var option: StructuredCommandOption
) : Builder<T>
{
    val nodes = mutableListOf<ArgumentNode>()

    fun add(name: String, predicate: (StructuredCommandBuilder.BranchBuilder) -> Unit): StructuredCommandBuilderBase<T>
    {
        val branch = StructuredCommandBuilder.BranchBuilder(name, serviceProvider, option)
        predicate(branch)
        nodes.add(branch.build())
        return this
    }

    fun add(executorNode: ArgumentExecutorNode) : StructuredCommandBuilderBase<T>
    {
        nodes.add(executorNode)
        return this
    }

    inline fun <reified TNode: ArgumentExecutorNode> add() : StructuredCommandBuilderBase<T>
    {
        val ctor = TNode::class.constructors.firstOrNull() ?:
            throw IllegalArgumentException("${TNode::class.simpleName} doesn't have any constructor.")
        val args = ctor.parameters.associateWith {
            val dependency = it.type.classifier as? KClass<*> ?:
            throw IllegalArgumentException("${it.name} was invalid.")
            serviceProvider.getRequiredService(dependency)
        }

        val instance = ctor.callBy(args)
        nodes.add(instance)

        return this
    }
}