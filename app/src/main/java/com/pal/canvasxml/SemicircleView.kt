package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat


class SemicircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColor)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    private val centerX: Float
        get() = width / 2f



    private val centerY: Float
        get() = height / 2f
         private val radius: Float
        get() = (width / 2f) - 10f.dpToPx()



    val startAngle = -180f
    val sweepAngle = 180f

    private val arcRect: RectF by lazy {
        RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }
    private val arcPaintForProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.arcColorProgress)
        style = Paint.Style.STROKE
        strokeWidth = 36f
        strokeCap = Paint.Cap.ROUND
    }





    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //val arcRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaintForProgress)
        //canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint)
        //canvas.drawArc(arcRect,startAngle,75f,false,arcPaintForProgress)
    }
    private fun Float.dpToPx(): Float {
        val scale = context.resources.displayMetrics.density
        return this * scale + 0.5f
    }
  /*  private var dynamicWeight = 1.0f
    private var dynamicHeight = ViewGroup.LayoutParams.WRAP_CONTENT

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SemicircleView)
        try {
            // Read the attributes from XML and set the corresponding properties
            dynamicWeight = typedArray.getFloat(R.styleable.SemicircleView_dynamicWeight, 1f)
            dynamicHeight = typedArray.getDimensionPixelSize(
                R.styleable.SemicircleView_dynamicHeight,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } finally {
            typedArray.recycle()
        }
    }

    // Override onMeasure to set the custom height and weight
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * dynamicWeight).toInt()

        // Set the calculated height for the custom view
        setMeasuredDimension(width, height)
    }
    */
}
