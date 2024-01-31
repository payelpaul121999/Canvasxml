package com.pal.canvasxml

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pal.canvasxml.service.ForegroundMusicService
import com.pal.canvasxml.service.ForegroundService

class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        val intent = Intent(this,ForegroundMusicService::class.java)
        startService(intent)
    }
}