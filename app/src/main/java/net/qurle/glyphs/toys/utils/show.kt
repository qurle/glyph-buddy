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


fun show(bitmap: Bitmap, c: Context, gmm: GlyphMatrixManager) {
    gmm.setMatrixFrame(
        GlyphMatrixFrame.Builder()
            .addTop(
                GlyphMatrixObject.Builder()
                    .setImageSource(bitmap)
                    .setScale(100)
                    .setOrientation(0)
                    .setPosition(0, 0)
                    .setReverse(false)
                    .build()
            )
            .build(c).render()
    )

}

fun show(drawable: Drawable, c: Context, gmm: GlyphMatrixManager) {
    show(drawable.toBitmap(), c, gmm)
}

fun show(drawableId: Int, c: Context, gmm: GlyphMatrixManager) {
    show(GlyphMatrixUtils.drawableToBitmap(ContextCompat.getDrawable(c, drawableId)), c, gmm)
}