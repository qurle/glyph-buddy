package net.qurle.glyphs.toys.buddy.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import net.qurle.glyphs.toys.buddy.Config
import net.qurle.glyphs.toys.buddy.State
import kotlin.random.Random

fun createBatteryReceiver(onStateChange: (State) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            getBatteryPercentage(context)
            val newState = when {
//                batteryPercent <= 20 -> State.TIRED
                else -> State.IDLE
            }
            onStateChange(newState)
        }
    }
}

fun getBatteryPercentage(context: Context): Int {
    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = context.registerReceiver(null, iFilter)
    val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level != -1 && scale != -1 && scale != 0) {
        (level * 100 / scale.toFloat()).toInt()
    } else {
        -1
    }
}

fun getTirednessLevel(context: Context): Int {
    val batteryPercent = getBatteryPercentage(context)
    return when {
        batteryPercent <= 3 -> 3
        batteryPercent <= 10 -> 2
        batteryPercent <= 20 -> 1
        else -> 0
    }
}

fun lookAroundDelay(): Long {
    val base = Config.LOOK_AROUND_INTERVAL
    val variation = (base * Config.LOOK_AROUND_VAR).toLong()
    return (base + Random.nextLong(-variation, variation))
}