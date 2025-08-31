package net.qurle.glyphs.toys.buddy.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import kotlin.math.roundToInt

enum class ScaleAnchor {
    DEFAULT, CENTER
}

/**
 * Utility class for creating and manipulating Drawables by combining multiple drawable resources.
 * This class allows for adding, transforming (scaling, mirroring, moving), and adjusting the opacity
 * of individual drawable components before composing them into a single Drawable.
 *
 * @property context The Android [Context] used for accessing resources.
 * @property size The target size (width and height) of the resulting Drawable in pixels.
 *                Defaults to 25 pixels.
 */
class DrawableBuilder(
    val context: Context,
    val size: Int = 25
) {
    private var bitmap = createBitmap(this.size, this.size, Bitmap.Config.ARGB_8888)
    private val drawables = mutableListOf<Drawable>()
    // Adds a drawable resource to the builder, positioning and scaling it as specified.

    fun add(
        @DrawableRes drawableId: Int,
        left: Int,
        top: Int,
        right: Int = -1,
        bottom: Int = -1
    ): DrawableBuilder {
        val drawable = ContextCompat.getDrawable(this.context, drawableId)
        return add(drawable, left, top, right, bottom)
    }

    // Adds an existing Drawable object to the builder, positioning and scaling it as specified.
    fun add(
        drawable: Drawable?,
        left: Int = 0,
        top: Int = 0,
        right: Int = -1,
        bottom: Int = -1
    ): DrawableBuilder {
        if (drawable == null) return this

        val density = context.resources.displayMetrics.density
        val width = (drawable.intrinsicWidth / density).roundToInt()
        val height = (drawable.intrinsicHeight / density).roundToInt()

        Log.d("q", "drawable size: $width × $height")
        drawable.setBounds(
            left,
            top,
            if (right < 0) left + width else right,
            if (bottom < 0) top + height else bottom
        )

        drawables.add(drawable)

        return this
    }

    // Mirrors the last added drawable horizontally across the center of the bitmap.
    fun mirror(): DrawableBuilder {
        if (drawables.isEmpty()) return this

        Log.d("q", "Drawable List: $drawables")
        val lastDrawable = drawables.last()
        lastDrawable.setBounds(
            size - lastDrawable.bounds.right,
            lastDrawable.bounds.top,
            size - lastDrawable.bounds.left,
            lastDrawable.bounds.bottom
        )
        return this
    }

    // Moves the last added drawable by the specified x and y offsets.
    fun move(x: Int, y: Int): DrawableBuilder {
        if (drawables.isEmpty()) return this

        val lastDrawable = drawables.last()

        lastDrawable.setBounds(
            lastDrawable.bounds.left + x,
            lastDrawable.bounds.top + y,
            lastDrawable.bounds.right + x,
            lastDrawable.bounds.bottom + y
        )
        return this
    }

    // Clones the last added drawable and adds the clone to the list of drawables.
    fun clone(): DrawableBuilder {
        if (drawables.isEmpty()) return this
        val lastDrawable = drawables.last()
        val clone = lastDrawable.constantState!!.newDrawable().mutate()
        clone.bounds = lastDrawable.bounds
        drawables.add(clone)
        return this
    }

    // Sets the opacity of the last added drawable.
    fun opacity(opacity: Float): DrawableBuilder {
        if (drawables.isEmpty()) return this
        val lastDrawable = drawables.last()
        lastDrawable.alpha = (255 * opacity.coerceIn(0f, 1f)).roundToInt()
        return this
    }

    // Scales the last added drawable by the specified factors, optionally anchoring the scale operation to the center.
    fun scale(
        scaleX: Float,
        scaleY: Float,
        anchor: ScaleAnchor = ScaleAnchor.DEFAULT
    ): DrawableBuilder {
        if (drawables.isEmpty()) return this
        val lastDrawable = drawables.last()
        val bounds = lastDrawable.bounds

        val newWidth = bounds.width() * scaleX
        val newHeight = bounds.height() * scaleY

        when (anchor) {
            ScaleAnchor.DEFAULT -> {
                lastDrawable.setBounds(
                    bounds.left,
                    bounds.top,
                    (bounds.left + newWidth).roundToInt(),
                    (bounds.top + newHeight).roundToInt()
                )
            }

            ScaleAnchor.CENTER -> {
                val dx = (bounds.width() - newWidth) / 2
                val dy = (bounds.height() - newHeight) / 2
                lastDrawable.setBounds(
                    (bounds.left + dx).roundToInt(),
                    (bounds.top + dy).roundToInt(),
                    (bounds.right - dx).roundToInt(),
                    (bounds.bottom - dy).roundToInt()
                )
            }
        }
        return this
    }

    // Builds the final Drawable by drawing all added drawables onto a bitmap and then resets the builder.
    fun build(): Drawable {
        val canvas = Canvas(bitmap)
        for (drawable in drawables) {
            drawable.draw(canvas)
        }
        val drawable = bitmap.toDrawable(context.resources)
        reset()
        return drawable
    }

    // Returns the current bitmap being used by the builder.
    fun bitmap(): Bitmap {
        return this.bitmap
    }

    // Resets the builder by clearing the list of drawables and creating a new bitmap.
    private fun reset(): DrawableBuilder {
        this.drawables.clear()
        this.bitmap = createBitmap(size, size, Bitmap.Config.ARGB_8888)
        return this
    }
}