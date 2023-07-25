package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View


class SemicircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width / 2f - 10
        val radiusForSmall = width / 2f - 40
        val radiusForSmallNumber = width / 2f - 37
        val startAngle = -180f
        val sweepAngle = 180f

        val paint = Paint().apply {
            color =  Color.parseColor("#D2EAE2")
            strokeWidth = 20f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.BUTT
        }

        // Draw the large semi-circle
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            startAngle, sweepAngle, false, paint
        )

        // Draw the small semi-circle with rounded caps
        paint.color = Color.parseColor("#429587")
        paint.strokeWidth = 36f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius,
            startAngle, 120f, false, paint
        )

        // Draw the solid center circle
        paint.color = android.graphics.Color.parseColor("#E2F2ED")

        canvas.drawArc(
            centerX - radiusForSmall, centerY - radiusForSmall,
            centerX + radiusForSmall, centerY + radiusForSmall,
            startAngle, sweepAngle, true, paint
        )

        // Draw the dashed center circle with rounded caps
        paint.color = Color.parseColor("#D2EAE2")
        paint.strokeWidth = 8f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            centerX - radiusForSmall, centerY - radiusForSmall,
            centerX + radiusForSmall, centerY + radiusForSmall,
            startAngle, sweepAngle, false, paint
        )

        // Draw the dot points on the arc
        val dotRadius = 35f
        val angleStep = sweepAngle / 4
        for (i in 0..4) {
            val angle = startAngle + angleStep * i
            val x = centerX + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

            paint.color = Color.parseColor("#FFFFFF")
            canvas.drawCircle(x, y, dotRadius, paint)
        }

        // Draw the custom triangle
        val trianglePath = Path()
        val angleInRadians = Math.toRadians((-100).toDouble()).toFloat()
        val x1 = centerX + radius * Math.cos(angleInRadians.toDouble()).toFloat()
        val y1 = centerY + radius * Math.sin(angleInRadians.toDouble()).toFloat()
        val x2 = x1 + 80f
        val y2 = y1
        val x3 = centerX
        val y3 = centerY - radius + 100f
        trianglePath.moveTo(x1, y1)
        trianglePath.lineTo(x3, y3)
        trianglePath.lineTo(x2, y2)
        trianglePath.close()

        paint.color = Color.BLUE
        canvas.drawPath(trianglePath, paint)

        // Draw the text inside the arc
        val fontSize = 20f
        paint.textSize = 14f
        paint.textAlign = Paint.Align.CENTER

        val textFirstItem = "TO RETAIN PLATINUM"
        val arcCenterYFirstItem = centerY - (radiusForSmall / 2) - 120f
        canvas.drawText(textFirstItem, centerX, arcCenterYFirstItem + (fontSize/ 2), paint)

        // Draw other text items...

        // Draw the text outside the arc...
    }
}
