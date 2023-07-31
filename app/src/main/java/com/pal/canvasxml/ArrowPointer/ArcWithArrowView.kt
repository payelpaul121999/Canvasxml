package com.pal.canvasxml.ArrowPointer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class ArcWithArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val arcPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val arrowPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = false
    }

    private val startAngle = -180f
    private val sweepAngle = 180f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(centerX, centerY) * 0.8).toFloat()

        // Calculate the start and end points of the arc
        val startX = centerX + radius * cos(Math.toRadians(startAngle.toDouble())).toFloat()
        val startY = centerY + radius * sin(Math.toRadians(startAngle.toDouble())).toFloat()
        val endX = centerX + radius * cos(Math.toRadians((startAngle + sweepAngle).toDouble())).toFloat()
        val endY = centerY + radius * sin(Math.toRadians((startAngle + sweepAngle).toDouble())).toFloat()

        // Draw the arc
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            startAngle,
            sweepAngle,
            false,
            arcPaint
        )

        // Calculate the coordinates of the arrowhead
        val arrowSize = 100f // Modify the size of the arrowhead as needed
        val arrowEndPointX = endX + arrowSize * cos(Math.toRadians((startAngle + sweepAngle - 20).toDouble())).toFloat()
        val arrowEndPointY = endY + arrowSize * sin(Math.toRadians((startAngle + sweepAngle - 20).toDouble())).toFloat()

        // Draw the arrowhead
        val path = Path().apply {
            moveTo(endX, endY)
            lineTo(arrowEndPointX, arrowEndPointY)
            lineTo(
                endX + arrowSize * cos(Math.toRadians((startAngle + sweepAngle + 20).toDouble())).toFloat(),
                endY + arrowSize * sin(Math.toRadians((startAngle + sweepAngle + 20).toDouble())).toFloat()
            )
            close()
        }
        canvas.drawPath(path, arrowPaint)
    }
}

