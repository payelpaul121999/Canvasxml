package com.pal.canvasxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MemberShipSemiCircleProgress>(R.id.circle).apply {
            setProgressCount(20)
        }
    }
}