package com.pal.canvasxml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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
    // Keep track of the bounding rectangles of the bitmaps
    private val bitmapRects = mutableListOf<RectF>()
    private var clickedPointIndex: Int = -1

    private var progressCount = 40
    // Draw the text inside the arc
    var textFirstItem = "TO RETAIN PLATINUM"
    var textSecItem = "You require the following or more:"
    var text3rdItem = "Nights: 10 or Spends: ₹ 50,000"
    var text4thItem = "by 31st December 2023"

    //Text Size
    private var textFirstItemSize :  Float = 0f
    private var textSecondItemSize :  Float = 0f
    private var text3rdItemSize :  Float = 0f
    private var text4thItemSize :  Float = 0f

    //Text Color
    private var textFirstTextColor : Int = R.color.textFirstColor
    private var textSecondTextColor : Int = R.color.textSecondColor
    private var text3rdTextColor : Int = R.color.text3rdColor
    private var text4thTextColor : Int = R.color.text4thColor

    // Replace with your drawable resource ID
    private val drawableResId = R.drawable.star_icon
    private val drawableResIdStart = R.drawable.ic_point_start_icon
    private val drawableResIdEnd = R.drawable.ic_point_g
    private val drawableProgressMarker = R.drawable.ic_cir_dot_icon
    private val drawableTriangle = R.drawable.trianlge_left


    private var drawableBitmapStartPoint: Bitmap? = null
    private var drawableBitmapEndPoint: Bitmap? = null
    private var drawableBitmapMiddlePoint: Bitmap? = null
    private var drawableBitmapProgress: Bitmap? = null
    private var drawableBitmapTriangle: Bitmap? = null

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
    val startAngle = -180f
    val sweepAngle = 180f
    val angleStep = sweepAngle / 4
    val sweepAngleForArrowPointer = 90f
    init {
        // Load the drawable image into a Bitmap
        drawableBitmapMiddlePoint = BitmapFactory.decodeResource(resources, drawableResId)
        drawableBitmapStartPoint = BitmapFactory.decodeResource(resources, drawableResIdStart)
        drawableBitmapEndPoint = BitmapFactory.decodeResource(resources, drawableResIdEnd)
        drawableBitmapProgress = BitmapFactory.decodeResource(resources, drawableProgressMarker)
        drawableBitmapTriangle = BitmapFactory.decodeResource(resources, drawableTriangle)

        // Add the bounding rectangles to the list during initialization
        for (i in 0..4) {
            val angle = startAngle + angleStep * i
            val x = centerX + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

            val drawableBitmap = when (i) {
                0 -> drawableBitmapStartPoint
                4 -> drawableBitmapEndPoint
                else -> drawableBitmapMiddlePoint
            }

            drawableBitmap?.let {
                val left = x - it.width / 2f
                val top = y - it.height / 2f
                val right = left + it.width
                val bottom = top + it.height

                val rect = RectF(left, top, right, bottom)
                bitmapRects.add(rect)
            }
        }

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
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SemicircleView,
            0, 0).apply {
            try {

                textFirstItemSize = getDimension(
                    R.styleable.SemicircleView_text_size_for_first_item,
                    14f)
                textSecondItemSize = getDimension(
                    R.styleable.SemicircleView_text_size_for_second_item,
                    11f)
                text3rdItemSize = getDimension(
                    R.styleable.SemicircleView_text_size_for_third_item,
                    14f)
                text4thItemSize = getDimension(
                    R.styleable.SemicircleView_text_size_for_forth_item,
                    12f)

                textFirstTextColor = getInt(
                   R.styleable.SemicircleView_text_color_for_first_item,
                   R.color.textFirstColor)
                textSecondTextColor = getInt(
                    R.styleable.SemicircleView_text_color_for_second_item,
                    R.color.textSecondColor)
                text3rdTextColor = getInt(
                    R.styleable.SemicircleView_text_color_for_third_item,
                    R.color.text3rdColor)
                text4thTextColor = getInt(
                    R.styleable.SemicircleView_text_color_for_forth_item,
                    R.color.text4thColor)

                /*titleTextFont = getInt(
                    R.styleable.GaugeView_title_text_font,
                    R.font.sf_pro_text_bold
                )*/
            }finally {
                recycle()
            }
        }
    }
    private val MARGIN_FORM_WIDTH = 10f
    private val centerX: Float
        get() = width / 2f



    private val radiusA: Float
        get() = (width / 4.7f)
    private val centerY: Float
        get() = height / 2f + radiusA

    private val valueSUBTRACT: Float
        get() = (width * 0.05f)

    private val radius: Float
        get() = (width / 2f) - valueSUBTRACT



    //40f
    private val radiusForSmall: Float
        get() = radius - (valueSUBTRACT + 30f).toInt()
       // get() = radius - (valueSUBTRACT * 4f).toInt()
   //37f
    private val radiusForSmallNumber: Float
        get() =  radius- (valueSUBTRACT + 27f).toInt()
    private val radiusForDash: Float
        get() = radius - (valueSUBTRACT + 18f).toInt()

    private val arcRect: RectF by lazy {
        RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }
    private val arcRectSmall: RectF by lazy {
        RectF(centerX - radiusForSmall, centerY - radiusForSmall, centerX + radiusForSmall, centerY + radiusForSmall)
    }
    private val arcRectSmallForDash: RectF by lazy {
        RectF(centerX - radiusForDash, centerY - radiusForDash, centerX + radiusForDash, centerY + radiusForDash)
    }
    private val arcRectSmallNumber: RectF by lazy {
        RectF(centerX - radiusForSmallNumber, centerY - radiusForSmallNumber, centerX + radiusForSmallNumber, centerY + radiusForSmallNumber)
    }
    private val arcPaintT = Paint().apply {
        color = ContextCompat.getColor(context, R.color.arcColorProgress)
        strokeWidth = 10f
        isAntiAlias = true
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

       Log.d("JAPAN","${radiusA} $width ${valueSUBTRACT.toInt()} ${(valueSUBTRACT * 4f).toInt()} $radiusForSmall $radius")

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)
        canvas.drawArc(arcRect,startAngle, angleValue(progressCount),false,arcPaintForProgress)

        // Calculate the bounding rectangle of the arc
        val left = centerX - radiusForSmall
        val top = centerY - radiusForSmall
        val right = centerX + radiusForSmall
        val bottom = centerY + radiusForSmall
        // Draw the arc with the gradient paint
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, arcPaintSolid)
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, arcPaintGradient)
        canvas.drawArc(arcRectSmall, startAngle, sweepAngle, false, arcPaintSmall)

        /* draw dot on the arc */
        drawableBitmapProgress?.let {
            val angle = startAngle + angleValue(progressCount)
            val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawBitmap(it, x - it.width / 2f, y - it.height / 2f, null)
        }

        /*drawableBitmapTriangle?.let {
            val angle = startAngle + angleValue(50)
            val x = centerX + radiusForDash * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radiusForDash * sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawBitmap(it, x - it.width / 2f, y - it.height / 2f, null)
        }*/


        // Draw the dot points on the arc
        for (i in 0..4) {
            val angle = startAngle + angleStep * i
            val x = centerX + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            //canvas.drawCircle(x, y, dotRadius, dotPaint)

            val drawableBitmap = when (i) {
                0 -> drawableBitmapStartPoint
                4 -> drawableBitmapEndPoint
                else -> drawableBitmapMiddlePoint
            }

            drawableBitmap?.let {
                val left = x - it.width / 2f
                val top = y - it.height / 2f
                canvas.drawBitmap(it, left, top, null)
            }
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


        /*// Create the path effect for the dashed line
        val dashWidth = 40f // Adjust the width of the dashes
        val gapWidth = 20f // Adjust the width of the gaps
        val pathEffect = DashPathEffect(floatArrayOf(dashWidth, gapWidth), 0f)
        // Set the path effect for the paint
        dashedArcPaint.pathEffect = pathEffect
        // Draw the dashed arc
        canvas.drawArc(arcRectSmallForDash, startAngle, sweepAngle, false, dashedArcPaint)
       */
        val endX = centerX  + radius * cos(Math.toRadians((startAngle + sweepAngleForArrowPointer).toDouble())).toFloat()
        val endY = centerY + radius * sin(Math.toRadians((startAngle + sweepAngleForArrowPointer).toDouble())).toFloat()

        // Calculate the coordinates of the arrowhead
        val arrowSize = 80f // Modify the size of the arrowhead as needed
        val arrowEndPointX = endX + arrowSize * cos(Math.toRadians((startAngle + sweepAngleForArrowPointer + 180).toDouble())).toFloat()
        val arrowEndPointY = endY + arrowSize * sin(Math.toRadians((startAngle + sweepAngleForArrowPointer + 180).toDouble())).toFloat()

        // Draw the arrowhead outline
        val path = Path().apply {
            moveTo(endX , endY )
            lineTo(arrowEndPointX, arrowEndPointY)
            lineTo(
                endX + arrowSize * cos(Math.toRadians((startAngle + sweepAngleForArrowPointer + 140).toDouble())).toFloat(),
                endY + arrowSize * sin(Math.toRadians((startAngle + sweepAngleForArrowPointer + 140).toDouble())).toFloat()
            )
            close()
        }
        // Rotate the arrow path according to the arc angle
        val angleDegrees = (startAngle + sweepAngleForArrowPointer + 90).toFloat()
        val matrix = Matrix()
        matrix.setRotate(angleDegrees, arrowEndPointX, arrowEndPointY)
        path.transform(matrix)
        canvas.drawPath(path, arcPaintT)

        // Calculate the center position of the arc
        val arcCenterX = centerX
        val arcCenterYFirstItem = centerY - (radiusForSmall / 1.5f)
        val arcCenterY2ndItem = centerY - (radiusForSmall / 2)
        val arcCenterY3rdItem = centerY - (radiusForSmall / 3.5f)
        val arcCenterY4thItem = centerY - (radiusForSmall / 10)



        val textX = arcCenterX - (25f / 2)

        drawTextOnCanvas(canvas, textFirstItem, textX + 20f, arcCenterYFirstItem, textFirstItemSize, ContextCompat.getColor(context!!,textFirstTextColor))
        drawTextOnCanvas(canvas, textSecItem, textX, arcCenterY2ndItem, textSecondItemSize, ContextCompat.getColor(context!!,textSecondTextColor))
        drawTextOnCanvas(canvas, text3rdItem, textX, arcCenterY3rdItem, text3rdItemSize, ContextCompat.getColor(context!!,text3rdTextColor))
        drawTextOnCanvas(canvas, text4thItem, textX, arcCenterY4thItem, text4thItemSize, ContextCompat.getColor(context!!,text4thTextColor))



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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                // Loop through the points to check if touch event is within any point
                for (i in 0..4) {
                    val angle = startAngle + angleStep * i
                    val pointX = centerX + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
                    val pointY = centerY + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

                    val drawableBitmap = when (i) {
                        0 -> drawableBitmapStartPoint
                        4 -> drawableBitmapEndPoint
                        else -> drawableBitmapMiddlePoint
                    }

                    drawableBitmap?.let {
                        val left = pointX - it.width / 2f
                        val top = pointY - it.height / 2f

                        if (x >= left && x <= left + it.width && y >= top && y <= top + it.height) {
                            clickedPointIndex = i
                            invalidate()
                            Log.d("JAPAN","Click Perform once $clickedPointIndex")
                            return true
                        }
                    }
                }

                clickedPointIndex = -1
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun angleValue(progressCount:Int): Float {
        return (9 / 5.0f) * progressCount
    }
    fun setProgressCount(progressCount: Int){
        this.progressCount = progressCount
        invalidate()
    }
}

