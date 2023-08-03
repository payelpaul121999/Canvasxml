package com.pal.canvasxml

/*import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin*/

/*
class ArcWithTriangleView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    private val trianglePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // Define the three vertices of the triangle with a single corner at the top
        val vertex1X = 100f
        val vertex1Y = 200f
        val vertex2X = 200f
        val vertex2Y = 200f
        val vertex3X = 150f
        val vertex3Y = 100f




        val trianglePath = Path()
        trianglePath.moveTo(vertex1X, vertex1Y) // Starting point (vertex 1)
        trianglePath.lineTo(vertex3X, vertex3Y) // Line to vertex 3
        trianglePath.lineTo(vertex2X, vertex2Y) // Line to vertex 2
        trianglePath.close() // Close the path to complete the triangle

        // Now, draw the triangle using the canvas
        canvas.drawPath(trianglePath, trianglePaint)



    }
}
*/
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class ArcWithPointerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // Arc properties
    private val arcStartAngle = -180f
    private val arcSweepAngle = 180f
    private val arcStrokeWidth = 10f
    private val arcColor = Color.BLUE

    // Pointer properties
    private val pointerLength = 100f
    private val pointerWidth = 6f
    private val pointerColor = Color.RED

    // Paints
    private val arcPaint = Paint().apply {
        color = arcColor
        style = Paint.Style.STROKE
        strokeWidth = arcStrokeWidth
        isAntiAlias = true
    }

    private val pointerPaint = Paint().apply {
        color = pointerColor
        style = Paint.Style.STROKE
        strokeWidth = pointerWidth
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius =  width / 2f
        val centerWidth = width / 8f
        Log.e("JAPAN","$centerWidth")
        val rect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rect, arcStartAngle, arcSweepAngle, false, arcPaint)

        // Calculate the pointer endpoint
        val pointerEndpointX = centerX + radius * cos(Math.toRadians(arcStartAngle + arcSweepAngle.toDouble())).toFloat()
        val pointerEndpointY = centerY + radius * sin(Math.toRadians(arcStartAngle + arcSweepAngle.toDouble())).toFloat()

        // Draw the pointer line
        canvas.drawLine(centerX + centerWidth, centerY + centerWidth, pointerEndpointX, pointerEndpointY , pointerPaint)
    }
}
