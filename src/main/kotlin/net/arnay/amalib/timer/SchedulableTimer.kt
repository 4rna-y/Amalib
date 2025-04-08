package net.arnay.amalib.timer

interface SchedulableTimer
{
    var currentTick: Int
    fun onTick()
}