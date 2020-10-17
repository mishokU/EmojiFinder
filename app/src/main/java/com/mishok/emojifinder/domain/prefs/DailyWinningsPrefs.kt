package com.mishok.emojifinder.domain.prefs

import android.app.Application
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DailyWinningsPrefs @Inject constructor(val application: Application) {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "daily"
    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun isNextDay(): Boolean {
        var day = getDay()
        if (!hasFirstTime()) {
            writeTime("first_time")
            return true
        } else {

            val firstTime = sharedPref.getLong("first_time", 0)
            val smfFirst = SimpleDateFormat("yyyy-MM-DD", Locale.getDefault())
            val smfSecond = SimpleDateFormat("yyyy-MM-DD", Locale.getDefault())

            val dateOne = smfFirst.format(Date(firstTime))
            val dateSecond = smfSecond.format(Date(System.currentTimeMillis()))

            return if (dateSecond > dateOne) {
                if (day >= 15) {
                    day = 1
                } else {
                    ++day
                }
                sharedPref.edit().putInt("day", day)
                writeTime("first_time")
                true
            } else {
                false
            }
        }
    }

    fun setDay(day: Int) {
        sharedPref.edit().putInt("day", day).apply()
    }

    fun getDay(): Int {
        return sharedPref.getInt("day", 1)
    }

    private fun writeTime(time: String) {
        sharedPref.edit().putLong(time, System.currentTimeMillis()).apply()
    }

    private fun hasFirstTime(): Boolean {
        return sharedPref.contains("first_time")
    }
}