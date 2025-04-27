package net.arnay.amalib.tick

abstract class TickScheduler(val period: Long)
{
    var tickCount = 0
    var cancelled = false


    open fun start() {}
    abstract fun tick()
}