package net.qurle.glyphs.toys.buddy

object Config {
    const val T = 5// Level of tempo
    const val C = 1 // Level of chaos

    const val LOOK_AROUND_INTERVAL = 5_000L / T
    const val LOOK_AROUND_VARIANCE = 0.05 * C
    const val LOOK_AROUND_DURATION = 3_500L

    const val BLINK_INTERVAL = 5_000L / T
    const val BLINK_VARIANCE = 0.1 * C
    const val BLINK_DURATION = 200L
    const val WINK_DURATION = 500L

    const val HAPPY_2_BATTERY_CHARGING_FROM = 85
    const val HAPPY_1_BATTERY_CHARGING_FROM = 60
    const val TIRED_1_BATTERY_BELOW = 20
    const val TIRED_2_BATTERY_BELOW = 10
    const val TIRED_3_BATTERY_BELOW = 3

    const val MATRIX_SIZE = 25
    const val BATTERY_PROGRESS_THICKNESS = 1.5f

    const val COLOR_PRIMARY = 0xFFFFFFFF.toInt()
    const val COLOR_SECONDARY = 0xFF666666.toInt()
}