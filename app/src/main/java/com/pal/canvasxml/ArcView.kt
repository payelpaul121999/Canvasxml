package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class ArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColor)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private val arcPaintForProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColorProgress)
        style = Paint.Style.STROKE
        strokeWidth = 36f
        strokeCap = Paint.Cap.ROUND
    }

    private val centerX: Float
        get() = width / 2f

    private val centerY: Float
        get() = height / 2f

    private val startAngle = -180f
    private val sweepAngle = 180f

    private lateinit var arcRect: RectF

    init {
        // Set any additional initialization code here, if needed.
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calculate the available height for drawing the arc
        val availableHeight = height - paddingTop - paddingBottom

        // Calculate the radius based on the available height
        val radius = (availableHeight / 2f) - 10f.dpToPx()

        // Calculate the centerY based on padding and the available height
        val centerY = paddingTop + (availableHeight / 2f)

        // Update the arcRect with the new dimensions
        arcRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)

        // Calculate the current progress angle based on the percentage (e.g., 75% in this case)
        val progressPercentage = 0.75f
        val progressAngle = sweepAngle * progressPercentage

        canvas.drawArc(arcRect, startAngle, progressAngle, false, arcPaintForProgress)
    }

    private fun Float.dpToPx(): Float {
        val scale = context.resources.displayMetrics.density
        return this * scale + 0.5f
    }
}
