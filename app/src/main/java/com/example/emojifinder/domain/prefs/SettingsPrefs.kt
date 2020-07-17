package com.example.emojifinder.domain.prefs

import android.app.Application
import android.content.SharedPreferences
import javax.inject.Inject

class SettingsPrefs @Inject constructor(application: Application) {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "settings"
    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun isPlayMusic() : Boolean {
        return sharedPref.getBoolean("music", true)
    }

    fun isNotificationsAvailable() : Boolean{
        return sharedPref.getBoolean("notifications", false)
    }

    fun changeMusic(play: Boolean) {
        sharedPref.edit().putBoolean("music", play).apply()
    }

    fun changeNotifications(notifications : Boolean){
        sharedPref.edit().putBoolean("notifications", notifications).apply()
    }

}