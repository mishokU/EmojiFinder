package com.example.emojifinder.domain.prefs

import android.app.Application
import android.content.SharedPreferences
import javax.inject.Inject

class NotificationAlarmPrefs @Inject constructor(val application: Application) {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "alarm"
    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun isStarted() : Boolean {
        return sharedPref.contains("start")
    }

    fun setStarted() {
        sharedPref.edit().putBoolean("start", true).apply()
    }
}