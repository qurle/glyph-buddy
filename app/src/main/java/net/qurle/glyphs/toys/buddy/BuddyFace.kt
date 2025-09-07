package net.qurle.glyphs.toys.buddy

import android.content.Context
import android.graphics.drawable.Drawable
import net.qurle.glyphs.R
import net.qurle.glyphs.toys.buddy.utils.DrawableBuilder
import kotlin.random.Random


class BuddyFace(context: Context) {

    private val drawableBuilder = DrawableBuilder(context)
    private val lEye = Eye(4, 9)
    private val rEye = Eye(15, 9)
    private var drawable = this.default()

    private val default = drawableBuilder
        .add(R.drawable.buddy_eye_default_open, lEye.left, lEye.top)
        .clone()
        .mirror()
        .build()

    private val closedR = drawableBuilder
        .add(R.drawable.buddy_eye_default_open, lEye.left, lEye.top)
        .add(R.drawable.buddy_eye_default_closed, rEye.left, rEye.top + 2)
        .build()

    fun default(): Drawable {
        drawable = drawableBuilder
            .add(R.drawable.buddy_eye_default_open, lEye.left, lEye.top)
            .clone()
            .mirror()
            .build()
        return drawable
    }

    fun wink(): Drawable {
        drawable = drawableBuilder
            .add(R.drawable.buddy_eye_default_open, lEye.left, lEye.top)
            .add(R.drawable.buddy_eye_default_closed, rEye.left, rEye.top)
            .build()
        return drawable
    }
//
//    fun blink(): Drawable {
//        return this
//    }


    fun lookAround(): Drawable {
        val randomNumber =
            Random.nextDouble()

        val direction = when {
            randomNumber < 0.4 -> -1     // left 40%
            randomNumber < 0.8 -> 1      // right 40%
            else -> 0                   // up 20%
        }

        return drawableBuilder
            .add(drawable, 0, 0, drawableBuilder.size, drawableBuilder.size)
            .move(2 * direction, if (direction == 0) -2 else 0)
            .build()
    }

    fun tired(tiredness: Int = 1): Drawable {
        when (tiredness) {
            1 -> {
                drawable = drawableBuilder
                    .add(R.drawable.buddy_eye_tired_1_open, lEye.left, lEye.top)
                    .clone()
                    .mirror()
                    .build()
                return drawable
            }

            2 -> {
                drawable = drawableBuilder
                    .add(R.drawable.buddy_eye_tired_2_open, lEye.left, lEye.top)
                    .move(1, 1)
                    .clone()
                    .mirror()
                    .build()
                return drawable
            }

            3 -> {
                drawable = drawableBuilder
                    .add(R.drawable.buddy_eye_tired_3, lEye.left, lEye.top)
                    .opacity(0.5f)
                    .move(1, 2)
                    .clone()
                    .mirror()
                    .build()
                return drawable
            }

            else -> return default()
        }
    }

}

class Eye(
    val left: Int,
    val top: Int,
    width: Int? = null,
    height: Int? = null
)