package net.qurle.glyphs.toys.buddy

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.nothing.ketchum.GlyphMatrixManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.qurle.glyphs.toys.buddy.utils.BatteryStatus
import net.qurle.glyphs.toys.utils.randomized
import net.qurle.glyphs.toys.utils.render

class EventHandler(
    private val scope: CoroutineScope,
    private val context: Context,
    private val gmm: GlyphMatrixManager
) {
    private var currentState: State = State.IDLE
    private var currentJob: Job? = null
    private var eventJob: Job? = null

    private val face = BuddyFace(context, gmm)

    private fun show(drawable: Drawable) {
        render(drawable, context, gmm)
    }

    fun startEventLoop() {
        currentJob?.cancel()
        handleStateChange(State.IDLE)
    }

    fun handleStateChange(
        state: State,
        mood: Mood = Mood.DEFAULT,
        batteryStatus: BatteryStatus = BatteryStatus()
    ) {
        if (state.priority >= currentState.priority) {
            currentJob?.cancel()
            currentState = state
            currentJob = scope.launch {
                when (state) {
                    State.IDLE -> {
                        render(
                            listOfNotNull(
                                face.drawMood(mood),
                                if (batteryStatus.charging) ArcDrawable(batteryStatus.percentage) else null
                            ), context, gmm
                        )

                        launch {
                            while (isActive && currentState == State.IDLE) {
                                val nextBlink =
                                    randomized(Config.BLINK_INTERVAL, Config.BLINK_VARIANCE)
                                Log.d("q", "Next blink in $nextBlink ms")
                                delay(nextBlink)
                                if (currentState == State.IDLE) {
                                    face.blink()
                                }
                            }
                        }

                        launch {
                            while (isActive && currentState == State.IDLE) {
                                face.drawMood(mood)
                                val nextLookAround = randomized(
                                    Config.LOOK_AROUND_INTERVAL,
                                    Config.LOOK_AROUND_VARIANCE
                                )
                                Log.d("q", "Next Lookaround in $nextLookAround ms")
                                delay(nextLookAround)
                                if (currentState == State.IDLE) {
                                    face.lookAround()
                                }
                            }
                        }
                    }

                    else -> false
                }
            }
        }
    }
}