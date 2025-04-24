package net.arnay.amalib.command

class StructuredCommandOptions
{
    var onArgumentInvalidMessage: (label: String, args: Array<out String>) -> String = { label, args ->
        "Command /${label} ${args.joinToString(" ")} was invalid"
    }

    var
}