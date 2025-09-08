import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import net.qurle.glyphs.toys.buddy.Config

class ArcDrawable(
    private var progress: Int = 0,
    private val strokeWidth: Float = Config.BATTERY_PROGRESS_THICKNESS,
    private val size: Int = Config.MATRIX_SIZE,
    private val arcColor: Int = Config.COLOR_SECONDARY
) : Drawable() {

    private val startAngle = -90f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = this@ArcDrawable.strokeWidth
        color = arcColor
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val rectF = RectF(
            bounds.left.toFloat(), bounds.top.toFloat(),
            bounds.right.toFloat(), bounds.bottom.toFloat()
        )
        val sweepAngle = (progress.coerceIn(0, 100) / 100f) * 360f
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
    }

    override fun getIntrinsicWidth(): Int {
        return size
    }

    override fun getIntrinsicHeight(): Int {
        return size
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}