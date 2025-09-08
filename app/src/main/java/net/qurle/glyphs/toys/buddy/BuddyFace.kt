package net.qurle.glyphs.toys.buddy

import android.content.Context
import android.graphics.drawable.Drawable
import com.nothing.ketchum.GlyphMatrixManager
import kotlinx.coroutines.delay
import net.qurle.glyphs.R
import net.qurle.glyphs.toys.buddy.utils.DrawableBuilder
import net.qurle.glyphs.toys.utils.render
import kotlin.random.Random


class BuddyFace(
    private val context: Context,
    private val gmm: GlyphMatrixManager?,
) {

    private val drawableBuilder = DrawableBuilder(context)
    private val lEyePos = Pair(4, 9)
    private val rEyePos = Pair(14, 9)

    private var mood = Mood.DEFAULT
    var faceDrawable = drawFace()


    fun drawMood(mood: Mood): Drawable {
        this.mood = mood
        return drawFace()
    }

    suspend fun wink() {
        drawFace(lEyeClosed = false, rEyeClosed = true)
        delay(Config.WINK_DURATION)
        drawFace()
    }

    suspend fun blink() {
        drawFace(lEyeClosed = true, rEyeClosed = true)
        delay(Config.BLINK_DURATION)
        drawFace()
    }

    suspend fun lookAround() {
        val randomNumber = Random.nextDouble()

        val direction = when {
            randomNumber < 0.4 -> -1    // left 40%
            randomNumber < 0.8 -> 1     // right 40%
            else -> 0                   // up 20%
        }

        render(
            drawableBuilder
                .add(faceDrawable, 0, 0, drawableBuilder.size, drawableBuilder.size)
                .move(2 * direction, if (direction == 0) -2 else 0)
                .build()
        )
        delay(Config.LOOK_AROUND_DURATION)
        drawFace()
    }

    private fun drawFace(
        mood: Mood = this.mood,
        lEyeClosed: Boolean = false,
        rEyeClosed: Boolean = false,
    ): Drawable {
        this.faceDrawable = drawableBuilder
            .add(getEyeId(mood, lEyeClosed), lEyePos.first, lEyePos.second)
            .add(getEyeId(mood, rEyeClosed), rEyePos.first, rEyePos.second)
            .build()
//        render(this.faceDrawable)
        return this.faceDrawable
    }

    private fun getEyeId(mood: Mood = this.mood, closed: Boolean = false): Int {
        val eyeDrawable = when (mood) {
            Mood.HAPPY_2 -> if (closed) R.drawable.buddy_eye_happy_2_closed else R.drawable.buddy_eye_happy_2
            Mood.HAPPY_1 -> if (closed) R.drawable.buddy_eye_happy_1_closed else R.drawable.buddy_eye_happy_1
            Mood.TIRED_1 -> if (closed) R.drawable.buddy_eye_tired_1_closed else R.drawable.buddy_eye_tired_1
            Mood.TIRED_2 -> if (closed) R.drawable.buddy_eye_tired_2_closed else R.drawable.buddy_eye_tired_2
            Mood.TIRED_3 -> R.drawable.buddy_eye_tired_3
            else -> if (closed) R.drawable.buddy_eye_default_closed else R.drawable.buddy_eye_default
        }
        return eyeDrawable
    }

    private fun render(drawable: Drawable = this.faceDrawable) {
        if (this.gmm === null) return
        render(drawable, this.context, this.gmm)
    }
}

