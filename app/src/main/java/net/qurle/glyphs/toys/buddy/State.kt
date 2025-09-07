package net.qurle.glyphs.toys.buddy

enum class State(val priority: Int) {
    IDLE(1),
    SLEEP(2),
    SNOOZY(3),
    DANCING(4),
    SHAKING(5)
}