package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.Math.abs
import kotlin.math.cos
import kotlin.math.sin

class MemberShipSemiCircleProgress@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColor)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    private val arcPaintSmall = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
    // Define dotColor in your colors.xml
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.DotColor)
        style = Paint.Style.FILL
    }
    private val arcPaintGradient: Paint

    private val arcPaintSolid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF") // Set your desired fill color here
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    init {
        // Create a gradient shader for the paint
        val gradientColors = intArrayOf(
            Color.parseColor("#F2F8F6"),
            Color.parseColor("#E2F2ED")

        )
        val gradientPositions = floatArrayOf(0f, 0.949f) // 0% and 94.9% positions
        val gradient = LinearGradient(
            centerX - radiusForSmall, centerY, centerX + radiusForSmall, centerY,
            gradientColors, gradientPositions, Shader.TileMode.CLAMP
        )


        // Set the gradient shader as the paint's color
        arcPaintGradient = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = gradient
        }
    }
    private val centerX: Float
        get() = width / 2f

    private val centerY: Float
        get() = height / 2f

    private val radius: Float
        get() = (width / 2f) - 10f.dpToPx()

    private val radiusForSmall: Float
        get() = (width / 2f) - 40f.dpToPx()

    private val radiusForSmallNumber: Float
        get() = (width / 2f) - 37f.dpToPx()

    private val arcRect: RectF by lazy {
        RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }
    private val arcRectSmall: RectF by lazy {
        RectF(centerX - radiusForSmall, centerY - radiusForSmall, centerX + radiusForSmall, centerY + radiusForSmall)
    }
    private val arcRectSmallNumber: RectF by lazy {
        RectF(centerX - radiusForSmallNumber, centerY - radiusForSmallNumber, centerX + radiusForSmallNumber, centerY + radiusForSmallNumber)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startAngle = -180f
        val sweepAngle = 180f
        val dotRadius = 35f
        val angleStep = sweepAngle / 4

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)
        canvas.drawArc(arcRect,startAngle,75f,false,arcPaintForProgress)

        // Calculate the bounding rectangle of the arc
        val left = centerX - radiusForSmall
        val top = centerY - radiusForSmall
        val right = centerX + radiusForSmall
        val bottom = centerY + radiusForSmall
        // Draw the arc with the gradient paint
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, arcPaintSolid)
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, arcPaintGradient)
        canvas.drawArc(arcRectSmall, startAngle, sweepAngle, false, arcPaintSmall)

        // Draw the dot points on the arc
        for (i in 0..4) {
            val angle = startAngle + angleStep * i
            val x = centerX + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawCircle(x, y, dotRadius, dotPaint)
        }
        // Calculate the angle step for the text positions
        val angleStepForText = sweepAngle / 5
        // Draw text along the arc
        for (i in 0..5) {
            val angle = startAngle + angleStepForText * i
            val x1 = centerX + radiusForSmallNumber * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y1 = centerY + radiusForSmallNumber * sin(Math.toRadians(angle.toDouble())).toFloat()
            drawTextOnCanvas(canvas, abs(i).toString(), x1, y1)
        }

        // Draw the text inside the arc
        val textFirstItem = "TO RETAIN PLATINUM"
        val textSecItem = "You require the following or more:"
        val text3rdItem = "Nights: 10 or Spends: ₹ 50,000"
        val text4thItem = "by 31st December 2023"

        // Calculate the center position of the arc
        val arcCenterX = centerX
        val arcCenterYFirstItem = centerY - (radiusForSmall / 2) - 100f
        val arcCenterY2ndItem = centerY - (radiusForSmall / 2) - 60f
        val arcCenterY3rdItem = centerY - (radiusForSmall / 4) - 50f
        val arcCenterY4thItem = centerY - (radiusForSmall / 10) - 20f

        val textX = arcCenterX - (25f / 2)

        drawTextOnCanvas(canvas, textFirstItem, textX + 20f, arcCenterYFirstItem, 14f, Color.parseColor("#2F2F2F"))
        drawTextOnCanvas(canvas, textSecItem, textX, arcCenterY2ndItem, 11f, Color.parseColor("#70968D"))
        drawTextOnCanvas(canvas, text3rdItem, textX, arcCenterY3rdItem, 14f, Color.parseColor("#2F2F2F"))
        drawTextOnCanvas(canvas, text4thItem, textX, arcCenterY4thItem, 12f, Color.parseColor("#9D9D9D"))
    }

    private fun Float.dpToPx(): Float {
        val scale = context.resources.displayMetrics.density
        return this * scale + 0.5f
    }
    private fun drawTextOnCanvas(canvas: Canvas, text: String, x: Float, y: Float) {
        canvas.drawText(text, x, y, Paint().apply {
            textSize = 30f
            color = Color.parseColor("#B29E7C")
            textAlign = Paint.Align.CENTER
        })
    }

    private fun drawTextOnCanvas(canvas: Canvas, text: String, x: Float, y: Float, textSize: Float, textColor: Int) {
        canvas.drawText(
            text,
            x,
            y,
            Paint().apply {
                this.textSize = textSize.dpToPx()
                textAlign = Paint.Align.CENTER
                color = textColor
               // typeface = ResourcesCompat.getFont(context, R.font.your_font) // Replace with your desired font
            }
        )
    }
}

