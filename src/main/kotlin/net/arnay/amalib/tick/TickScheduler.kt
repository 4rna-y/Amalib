package net.arnay.amalib.tick

abstract class TickScheduler(val period: Long, val maxTime: Int)
{
    companion object Const
    {
        val INFINITE_MAX_TIME = -1
    }
    var tickCount = 0
    var cancelled = false
    var isPausing = false
    var isTerminated = false

    open fun onStart() {}
    abstract fun onTick()
    fun pause()
    {
        isPausing = true
    }
    open fun onComplete() {}
    fun terminate()
    {
        isTerminated = true
    }
    open fun onTerminate() {}

}