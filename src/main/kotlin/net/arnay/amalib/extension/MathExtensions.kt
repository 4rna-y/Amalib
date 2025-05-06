package net.arnay.amalib.extension

import kotlin.math.PI
import kotlin.math.sqrt

fun Double.rad(): Double = this * PI / 180.0
fun Double.deg(): Double = this * 180.0 / PI

fun Float.rad(): Float = this * PI.toFloat() / 180.0f
fun Float.deg(): Float = this * 180.0f / PI.toFloat()
fun Float.sqrt(): Float = sqrt(this)