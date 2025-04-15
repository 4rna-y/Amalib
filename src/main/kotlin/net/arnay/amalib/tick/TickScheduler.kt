package net.arnay.amalib.tick

abstract class TickScheduler
{
    abstract val period: Long

    var tickCount = 0
    var cancelled = false

    abstract fun tick()
}