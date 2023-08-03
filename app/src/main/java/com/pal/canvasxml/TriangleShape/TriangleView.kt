package com.pal.canvasxml.TriangleShape


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ArcWithArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var arrowSize = 66f

    // Angle in degrees for rotating the arrow 180 (degree rotation left) to 360 (degree rotation from right)
    private var arrowAngle = 330f

    fun setArrowAngle(angle: Float) {
        arrowAngle = angle
        invalidate() // Redraw the view when the angle changes
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate the center of the view
        centerX = width / 2f
        centerY = height / 2f

        // Set the radius of the arc to be the minimum between half of the width and height
        radius = minOf(width, height) / 2f - arrowSize

        // Draw the arc
        canvas.drawArc(
            centerX - radius, centerY - radius, centerX + radius, centerY + radius,
            -180f, 180f, false, arcPaint
        )

        // Calculate the coordinates of the arrow pointer
        val arrowX = centerX + radius * Math.cos(Math.toRadians(arrowAngle.toDouble())).toFloat()
        val arrowY = centerY + radius * Math.sin(Math.toRadians(arrowAngle.toDouble())).toFloat()

        // Save the current canvas state to restore it later
        canvas.save()

        // Translate and rotate the canvas to draw the arrow at the correct position and angle
        canvas.translate(arrowX, arrowY)
        canvas.rotate(arrowAngle, 0f, 0f)

        // Draw the arrow pointer
        val arrowPath = Path()
        arrowPath.moveTo(0f, 0f)
        arrowPath.lineTo(-arrowSize,- arrowSize)
        arrowPath.lineTo(-arrowSize, arrowSize)
        arrowPath.close()
        canvas.drawPath(arrowPath, arrowPaint)

        // Restore the canvas to its original state
        canvas.restore()
    }
}

