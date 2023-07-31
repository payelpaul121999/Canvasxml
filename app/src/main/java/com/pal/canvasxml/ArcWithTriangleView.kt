package com.pal.canvasxml

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

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
