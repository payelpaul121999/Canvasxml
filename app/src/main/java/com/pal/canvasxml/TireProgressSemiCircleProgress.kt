package com.itc.oneapp

/*import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.itc.oneapp.FandB.utilities.LogMessage
import java.lang.Math.abs
import kotlin.math.cos
import kotlin.math.sin

class TireProgressSemiCircleProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressCount = 80
    private val drawableResIdGold = R.drawable.milestone3
    private val drawableResIdMember = R.drawable.milestone1
    private val drawableResIdPlatinum = R.drawable.milestone4
    private val drawableResIdSilver = R.drawable.milestone2
    private val drawableProgressMarker = R.drawable.progress_marker

    private var drawableBitmapGold: Bitmap? = null
    private var drawableBitmapMember: Bitmap? = null
    private var drawableBitmapSilver: Bitmap? = null
    private var drawableBitmapPlatinum: Bitmap? = null
    private var drawableBitmapProgress: Bitmap? = null

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
        //strokeCap = Paint.Cap.ROUND
        strokeCap = Paint.Cap.BUTT
    }

    // Define dotColor in your colors.xml
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.DotColor)
        style = Paint.Style.FILL
    }
    private val dashedArcPaint = Paint().apply {
        color = Color.parseColor("#B29E7C")
        style = Paint.Style.STROKE
        strokeWidth = 5f // Adjust the thickness of the stroke
    }
    private val arcPaintGradient: Paint

    private val arcPaintSolid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF") // Set your desired fill color here
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private val arcPaintDashed = Paint().apply {
        color = ContextCompat.getColor(context, R.color.DashColor)
        style = Paint.Style.STROKE
        strokeWidth = 5f
        //setARGB(255, 0, 0, 0)
        pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0f)
    }

    init {
        // Load the drawable image into a Bitmap
        drawableBitmapSilver = BitmapFactory.decodeResource(resources, drawableResIdSilver)
        drawableBitmapGold = BitmapFactory.decodeResource(resources, drawableResIdGold)
        drawableBitmapMember = BitmapFactory.decodeResource(resources, drawableResIdMember)
        drawableBitmapPlatinum = BitmapFactory.decodeResource(resources, drawableResIdPlatinum)
        drawableBitmapProgress = BitmapFactory.decodeResource(resources, drawableProgressMarker)

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
        get() = (width / 2f) - 20f.dpToPx()

    private val radiusForSmall: Float
        get() = (width / 2f) - 40f.dpToPx()

    private val radiusForDashArc: Float
        get() = (width / 2f) - 25f.dpToPx()

    private val radiusForSmallNumber: Float
        get() = (width / 2f) - 37f.dpToPx()

    private val radiusForDash: Float
        get() = (width / 2f) - 28f.dpToPx()

    private val arcRectDashArc: RectF by lazy {
        RectF(
            centerX - radiusForDashArc,
            centerY - radiusForDashArc,
            centerX + radiusForDashArc,
            centerY + radiusForDashArc
        )
    }

    private val arcRect: RectF by lazy {
        RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    private val arcRectSmall: RectF by lazy {
        RectF(
            centerX - radiusForSmall,
            centerY - radiusForSmall,
            centerX + radiusForSmall,
            centerY + radiusForSmall
        )
    }
    private val arcRectSmallForDash: RectF by lazy {
        RectF(
            centerX - radiusForDash,
            centerY - radiusForDash,
            centerX + radiusForDash,
            centerY + radiusForDash
        )
    }
    private val arcRectSmallNumber: RectF by lazy {
        RectF(
            centerX - radiusForSmallNumber,
            centerY - radiusForSmallNumber,
            centerX + radiusForSmallNumber,
            centerY + radiusForSmallNumber
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val startAngle = -180f
        val sweepAngle = 180f
        val dotRadius = 35f
        val angleStep = sweepAngle / 3

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)
        canvas.drawArc(arcRect, startAngle, angleValue(), false, arcPaintForProgress)
        //canvas.drawArc(arcRectDashArc, startAngle, sweepAngle, false, arcPaintDashed)

        // Calculate the bounding rectangle of the arc
        val left = centerX - radiusForSmall
        val top = centerY - radiusForSmall
        val right = centerX + radiusForSmall
        val bottom = centerY + radiusForSmall
        // Draw the arc with the gradient paint
        //canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, arcPaintSolid)
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, arcPaintGradient)
        canvas.drawArc(arcRectSmall, startAngle, sweepAngle, false, arcPaintSmall)

        // Draw the dot points on the arc
        for (i in 0..3) {
            val angle = startAngle + angleStep * i
            val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

            val drawableBitmap = when (i) {
                0 -> drawableBitmapMember
                1 -> drawableBitmapSilver
                2 -> drawableBitmapGold
                else -> drawableBitmapPlatinum
            }

            drawableBitmap?.let {
                val left = x - it.width / 2f
                val top = y - it.height / 2f
                canvas.drawBitmap(it, left, top, null)
            }
        }

        *//* draw dot on the arc *//*
        drawableBitmapProgress?.let {
            val angle = startAngle + angleValue()
            val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawBitmap(it, x - it.width / 2f, y - it.height / 2f, null)
        }

        // Draw the text inside the arc
        val textFirstItem = "Upgrade to GOLD"
        val textSecItem = "You require the following or more:"
        val text3rdItem = "Nights: 10 or Spends: ₹ 50,000"
        val text4thItem = "by 31st December 2023"

        // Calculate the center position of the arc
        LogMessage("@@@", "$centerX  $radiusForSmall")
        val arcCenterX = centerX
        val arcCenterYFirstItem = centerY - (radiusForSmall / 1.5f)
        val arcCenterY2ndItem = centerY - (radiusForSmall / 2)
        val arcCenterY3rdItem = centerY - (radiusForSmall / 3.5f)
        val arcCenterY4thItem = centerY - (radiusForSmall / 10)

        val textX = arcCenterX - (25f / 2)

        drawTextOnCanvas(
            canvas,
            textFirstItem,
            textX,
            arcCenterYFirstItem,
            14f,
            Color.parseColor("#2F2F2F")
        )
        drawTextOnCanvas(
            canvas,
            textSecItem,
            textX,
            arcCenterY2ndItem,
            11f,
            Color.parseColor("#70968D")
        )
        drawTextOnCanvas(
            canvas,
            text3rdItem,
            textX,
            arcCenterY3rdItem,
            14f,
            Color.parseColor("#2F2F2F")
        )
        drawTextOnCanvas(
            canvas,
            text4thItem,
            textX,
            arcCenterY4thItem,
            12f,
            Color.parseColor("#9D9D9D")
        )
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

    private fun drawTextOnCanvas(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        textSize: Float,
        textColor: Int
    ) {
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

    private fun angleValue(): Float {
        return (9 / 5.0f) * progressCount
    }
}*/

