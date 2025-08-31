package net.qurle.glyphs.toys.buddy.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

fun getBatteryPercentage(context: Context): Int {
    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = context.registerReceiver(null, iFilter)

    val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

    return if (level != -1 && scale != -1 && scale != 0) {
        (level * 100 / scale.toFloat()).toInt()
    } else {
        -1 // Indicate an error or inability to get battery level
    }
}