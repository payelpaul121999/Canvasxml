package com.pal.canvasxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MemberShipSemiCircleProgress>(R.id.circle).apply {
            setProgressCount(20)
            Log.d("JAPAN","DATA")
        }
    }
}