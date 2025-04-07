package net.arnay.amalib.command.impl

import net.arnay.amalib.command.CommandObject
import net.arnay.amalib.command.node.ArgumentNode

class StructuredCommandObject(val name: String, nodes: MutableList<ArgumentNode>) : CommandObject
{

}