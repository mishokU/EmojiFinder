package com.example.emojifinder.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.emojifinder.R
import com.example.emojifinder.ui.main.SplashActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.

            val repeatingIntent = Intent(context, SplashActivity::class.java)
            repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val pendingIntent = PendingIntent.getActivity(
                context,
                100,
                repeatingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val newMessageNotification = NotificationCompat.Builder(context, "100")
                .setContentTitle(context.resources.getString(R.string.daily_title))
                .setContentText(context.resources.getString(R.string.daily_description))
                .setSmallIcon(R.drawable.icons8_man_detective_48px)
                .setColor(context.resources.getColor(R.color.main_color))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            // Issue the notification.
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(100, newMessageNotification)
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "daily"
            val descriptionText = "Prize"
            val id = "100"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(100)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}