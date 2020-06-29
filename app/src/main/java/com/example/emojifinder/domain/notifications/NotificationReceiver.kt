package com.example.emojifinder.domain.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.emojifinder.R
import com.example.emojifinder.ui.main.SplashActivity

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        val repeatingIntent = Intent(context, SplashActivity::class.java)
        repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            repeatingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val newMessageNotification =  Notification.Builder(context, "d")
            .setContentTitle(context.resources.getString(R.string.daily_title))
            .setContentText(context.resources.getString(R.string.daily_description))
            .setSmallIcon(R.drawable.icons8_man_detective_48px)
            .setColor(context.resources.getColor(R.color.main_color))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Issue the notification.
        //val notificationManager = NotificationManagerCompat.from(context)
        with(NotificationManagerCompat.from(context)) {
            notify(100, newMessageNotification)
        }
        //notificationManager.notify(100, newMessageNotification)
    }

}