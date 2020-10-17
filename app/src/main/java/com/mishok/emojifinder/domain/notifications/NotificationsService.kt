package com.mishok.emojifinder.domain.notifications

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import java.util.*
import javax.inject.Inject

class NotificationsService @Inject constructor(val application: Application) {

    fun create() {
        initCalendar()
    }

    private fun initCalendar() {
        val calendar: Calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1)

        Log.d("minute", calendar.get(Calendar.HOUR).toString())
        Log.d("minute", (calendar.get(Calendar.MINUTE)).toString())

        val intent = Intent(application, NotificationReceiver::class.java)
        intent.action = "notifyDaily"

        val pendingIntent = PendingIntent.getBroadcast(
            application,
            100,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}