package net.qurle.glyphs.toys.buddy

import android.content.Context
import android.graphics.drawable.Drawable
import com.nothing.ketchum.GlyphMatrixManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.qurle.glyphs.toys.buddy.utils.getBatteryPercentage
import net.qurle.glyphs.toys.buddy.utils.lookAroundDelay
import net.qurle.glyphs.toys.utils.render

class EventHandler(
    private val scope: CoroutineScope,
    private val context: Context,
    private val gmm: GlyphMatrixManager
) {
    private var currentState: State = State.IDLE
    private var currentJob: Job? = null
    private var eventJob: Job? = null

    private fun show(drawable: Drawable) {
        render(drawable, context, gmm)
    }

    fun startEventLoop() {
        eventJob?.cancel()
        eventJob = scope.launch {
            while (isActive) {
                getBatteryPercentage(context)
                val newState = State.IDLE
//                val newState = when {
//                    batteryPercent <= 20 -> State.TIRED
//                    else -> State.IDLE
//                }
                handleStateChange(newState)
                delay(lookAroundDelay())
                handleStateChange(State.IDLE, isSideEye = true)
                delay(Config.LOOK_AROUND_DURATION)
                handleStateChange(State.IDLE)
                delay(Config.BATTERY_CHECK)
            }
        }
    }

    fun handleStateChange(state: State, isSideEye: Boolean = false) {
        if (state.priority >= currentState.priority) {
            currentJob?.cancel()
            currentState = state
            val face = BuddyFace(context)
            currentJob = scope.launch {
                when (state) {
                    State.IDLE -> {
                        if (isSideEye) show(face.lookAround()) else show(face.default())
                    }

                    else -> false
//                    State.TIRED -> show(face.tired(getTirednessLevel(context)))
//                    State.WINK -> {
//                        show(face.wink())
//                        delay(500)
//                        show(face.default())
//                        currentState = State.IDLE
//                    }
                }
            }
        }
    }
}