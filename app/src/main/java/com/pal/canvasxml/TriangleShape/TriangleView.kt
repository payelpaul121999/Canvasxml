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
    private val valueSUBTRACT: Float
        get() = (width * 0.05f)
    private val centerPointerX: Float
        get() = width / 2f
    private val centerPointerY :Float
    get() = height / 2f

    private var arrowSize = 66f
    private val radiusArrowArc : Float
    get() = (width / 2f) - (valueSUBTRACT + 30f).toInt()

    private var arrowAngle = 330f

    fun setArrowAngle(angle: Float) {
        arrowAngle = angle
        invalidate()
    }
    private val arcRect: RectF by lazy {
        RectF(centerPointerX - radiusArrowArc, centerPointerY - radiusArrowArc, centerPointerX + radiusArrowArc, centerPointerY + radiusArrowArc)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)




        // Draw the arc
        canvas.drawArc(
            centerPointerX - radiusArrowArc, centerPointerY - radiusArrowArc, centerPointerX + radiusArrowArc, centerPointerY + radiusArrowArc,
            -180f, 180f, false, arcPaint
        )

        // Calculate the coordinates of the arrow pointer
        val arrowX = centerPointerX + radiusArrowArc * Math.cos(Math.toRadians(arrowAngle.toDouble())).toFloat()
        val arrowY = centerPointerY + radiusArrowArc * Math.sin(Math.toRadians(arrowAngle.toDouble())).toFloat()
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
        canvas.restore()
    }
}

