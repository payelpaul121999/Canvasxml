package com.pal.canvasxml.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.pal.canvasxml.NewActivity
import com.pal.canvasxml.R

class ForegroundService : Service() {
    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }
    override fun onBind(p0: Intent?): IBinder? {
       return null
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1,notification)
        return START_NOT_STICKY/*super.onStartCommand(intent, flags, startId)*/
    }


    private fun createNotification() : Notification{
        val notificationIntent = Intent(this,NewActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Running....****Payel")
            .setSmallIcon(R.drawable.ic_point_g)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Payel Paul",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

}