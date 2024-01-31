package com.pal.canvasxml.service

// ForegroundMusicService.kt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.pal.canvasxml.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class ForegroundMusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    companion object {
        const val CHANNEL_ID = "ForegroundMusicServiceChannel"
        const val ACTION_PLAY_PAUSE = "playPauseAction"
        const val ACTION_FORWARD = "forwardAction"
        const val ACTION_BACKWARD = "backwardAction"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer.create(this, R.raw.sample)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY_PAUSE -> handlePlayPauseAction()
            ACTION_FORWARD -> handleForwardAction()
            ACTION_BACKWARD -> handleBackwardAction()
        }

        val notification = createNotification()
        startForeground(1, notification)

        // Start playing music
        mediaPlayer.start()
        isPlaying = true

        // Simulate a long-running operation (replace with actual work)
        GlobalScope.launch {
            delay(5.minutes) // Simulate 1 minute of playback
            stopSelf()   // Stop the service after playback (you can handle this differently)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        // Create an intent for the play/pause action
        val playPauseIntent = Intent(this, ForegroundMusicService::class.java).apply {
            action = ACTION_PLAY_PAUSE
        }
        val playPausePendingIntent = PendingIntent.getService(
            this,
            0,
            playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create an intent for the forward action
        val forwardIntent = Intent(this, ForegroundMusicService::class.java).apply {
            action = ACTION_FORWARD
        }
        val forwardPendingIntent = PendingIntent.getService(
            this,
            0,
            forwardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create an intent for the backward action
        val backwardIntent = Intent(this, ForegroundMusicService::class.java).apply {
            action = ACTION_BACKWARD
        }
        val backwardPendingIntent = PendingIntent.getService(
            this,
            0,
            backwardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create a RemoteViews object for custom notification layout
        val remoteViews = RemoteViews(packageName, R.layout.custom_notification_layout)

        // Set up click handling for play/pause, forward, and backward buttons
        remoteViews.setOnClickPendingIntent(R.id.playPauseButton, playPausePendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.forwardButton, forwardPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.backwardButton, backwardPendingIntent)

        // Set up other elements in the custom layout if needed

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Service")
            .setContentText("Playing...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(true)
            .build()

        return notification
    }

    private fun handlePlayPauseAction() {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying

        updateNotification()
    }

    private fun handleForwardAction() {
        if (isPlaying) {
            val newPosition = mediaPlayer.currentPosition + 10000 // 10 seconds forward
            if (newPosition < mediaPlayer.duration) {
                mediaPlayer.seekTo(newPosition)
            } else {
                mediaPlayer.seekTo(mediaPlayer.duration) // Move to the end
                handlePlayPauseAction() // Pause if attempting to seek beyond the end
            }
        }
    }

    private fun handleBackwardAction() {
        if (isPlaying) {
            val newPosition = mediaPlayer.currentPosition - 10000 // 10 seconds backward
            if (newPosition >= 0) {
                mediaPlayer.seekTo(newPosition)
            } else {
                mediaPlayer.seekTo(0) // Move to the beginning
            }
        }
    }

    private fun updateNotification() {
        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.notify(1, createNotification())
    }
}
