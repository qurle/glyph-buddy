package net.qurle.glyphs.toys.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.nothing.ketchum.GlyphMatrixFrame
import com.nothing.ketchum.GlyphMatrixManager
import com.nothing.ketchum.GlyphMatrixObject
import com.nothing.ketchum.GlyphMatrixUtils

fun renderBitmaps(bitmaps: List<Bitmap>, c: Context, gmm: GlyphMatrixManager) {
    val builder = GlyphMatrixFrame.Builder()
    bitmaps.forEachIndexed { index, bitmap ->
        val objectBuilder = GlyphMatrixObject.Builder()
            .setImageSource(bitmap)
            .setScale(100)
            .setOrientation(0)
            .setPosition(0, 0)
            .setReverse(false)
            .build()
        when (index) {
            0 -> builder.addLow(objectBuilder)
            1 -> builder.addMid(objectBuilder)
            2 -> builder.addTop(objectBuilder)
        }
    }
    gmm.setMatrixFrame(builder.build(c).render())
}

fun render(drawables: List<Drawable>, c: Context, gmm: GlyphMatrixManager) {
    renderBitmaps(drawables.map { it.toBitmap() }, c, gmm)
}

fun render(bitmap: Bitmap, c: Context, gmm: GlyphMatrixManager) {
    renderBitmaps(listOf(bitmap), c, gmm)
}

fun render(drawable: Drawable, c: Context, gmm: GlyphMatrixManager) {
    render(drawable.toBitmap(), c, gmm)
}

fun render(drawableId: Int, c: Context, gmm: GlyphMatrixManager) {
    render(GlyphMatrixUtils.drawableToBitmap(ContextCompat.getDrawable(c, drawableId)), c, gmm)
}