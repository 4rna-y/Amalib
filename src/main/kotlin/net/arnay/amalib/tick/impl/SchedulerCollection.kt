package net.arnay.amalib.tick.impl

import net.arnay.amalib.tick.TickScheduler

class SchedulerCollection
{
    val schedulers = mutableMapOf<Int, TickScheduler>()
}