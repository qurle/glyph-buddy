package net.qurle.glyphs.toys.buddy.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import net.qurle.glyphs.toys.buddy.Config
import net.qurle.glyphs.toys.buddy.Mood
import net.qurle.glyphs.toys.buddy.State

fun createBatteryReceiver(onStateChange: (State, Mood, BatteryStatus) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val batteryStatus = getBatteryStatus(context)
            onStateChange(State.IDLE, batteryToMood(context, batteryStatus), batteryStatus)
        }
    }
}

fun batteryToMood(context: Context, batteryStatus: BatteryStatus? = null): Mood {
    val battery = batteryStatus ?: getBatteryStatus(context)
    return when {
        battery.charging && battery.percentage >= Config.HAPPY_2_BATTERY_CHARGING_FROM -> Mood.HAPPY_2
        battery.charging && battery.percentage >= Config.HAPPY_1_BATTERY_CHARGING_FROM -> Mood.HAPPY_1
        battery.percentage <= Config.TIRED_3_BATTERY_BELOW -> Mood.TIRED_3
        battery.percentage <= Config.TIRED_2_BATTERY_BELOW -> Mood.TIRED_2
        battery.percentage <= Config.TIRED_1_BATTERY_BELOW -> Mood.TIRED_1
        else -> Mood.DEFAULT
    }
}

private fun getBatteryStatus(context: Context): BatteryStatus {
    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = context.registerReceiver(null, iFilter)
    val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val percentage = if (level != -1 && scale != -1 && scale != 0) {
        (level * 100 / scale.toFloat()).toInt()
    } else -1
    val charging = batteryStatus?.getIntExtra(
        BatteryManager.EXTRA_STATUS,
        -1
    ) == BatteryManager.BATTERY_STATUS_CHARGING
    return BatteryStatus(percentage, charging)
}

class BatteryStatus(
    var percentage: Int = 0,
    var charging: Boolean = false
)
