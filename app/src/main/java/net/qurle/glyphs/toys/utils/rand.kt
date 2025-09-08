package net.qurle.glyphs.toys.utils

import kotlin.random.Random


fun randomized(interval: Long, variation: Double = .05): Long {
    val base = interval
    val variation = (base * variation).toLong()
    return (base + Random.nextLong(-variation, variation))
}