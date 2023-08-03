package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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



    private val radiusSmall: Float
        get() = (width / 2f) - 120f
    private val radius: Float
        get() = (width / 2f) - 30f
    private val centerY: Float
        get() = height / 2f
    private val centerX: Float
        get() = width / 2f

    private val arcRect: RectF by lazy {
        RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }
    private val arcRectSmall: RectF by lazy {
        RectF(centerX - radiusSmall, centerY - radiusSmall, centerX + radiusSmall, centerY + radiusSmall)
    }
    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColor)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    private val arcPaintSmaller = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColor)
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val arcPaintForProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColorProgress)
        style = Paint.Style.STROKE
        strokeWidth = 36f
        strokeCap = Paint.Cap.ROUND
    }
    val startAngle = -180f
    val sweepAngle = 180f



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)
        canvas.drawArc(arcRect, startAngle, 70f, false, arcPaintForProgress)

        canvas.drawArc(arcRectSmall, startAngle, sweepAngle, false, arcPaintSmaller)



    }


}
