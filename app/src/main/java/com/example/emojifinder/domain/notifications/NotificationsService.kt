package com.example.emojifinder.domain.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.emojifinder.R
import com.example.emojifinder.ui.main.MainActivity

class NotificationsService : Service() {



    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val input : String = intent?.getStringExtra("input") ?: "hi"
        val notifications : Notification = NotificationCompat.Builder(applicationContext, "0")
            .setContentTitle("Titlte")
            .setContentText(input)
            .setSmallIcon(R.drawable.emoji_finder)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notifications)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}