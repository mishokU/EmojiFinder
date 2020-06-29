package com.example.emojifinder.domain.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.emojifinder.ui.application.MainApplication
import java.util.*

class NotificationsService(val application: Application) {

    init {
        createNotificationChannel()
        initCalendar()
    }

    private fun initCalendar() {
        val calendar : Calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))

        val intent = Intent(application, NotificationReceiver::class.java)
        intent.action = "notifyDaily"

        val pendingIntent = PendingIntent.getBroadcast(
            application,
            100,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

     private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "DEDW"
            val descriptionText = "fefe"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("d", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}