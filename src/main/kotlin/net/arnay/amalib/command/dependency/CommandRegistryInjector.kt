package net.arnay.amalib.command.dependency

import net.arnay.amalib.command.StructuredCommandRegistry
import net.arnay.amalib.command.impl.StructuredCommandRegistryImpl
import net.arnay.amalib.dependency.ServiceCollection

fun ServiceCollection.useStructuredCommand() : ServiceCollection
{
    return addSingleton<StructuredCommandRegistry, StructuredCommandRegistryImpl>()
}