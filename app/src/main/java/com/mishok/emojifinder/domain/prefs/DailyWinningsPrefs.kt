package com.mishok.emojifinder.domain.prefs

import android.app.Application
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DailyWinningsPrefs @Inject constructor(val application: Application) {

    private val sharedPref: SharedPreferences = application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun isNextDay(): Boolean {
        var day = getDay()
        if (!hasFirstTime()) {
            writeTime(KEY_FIRST_TIME)
            return true
        } else {

            val firstTime = sharedPref.getLong(KEY_FIRST_TIME, 0)
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
                sharedPref.edit().putInt(KEY_DAY, day)
                writeTime(KEY_FIRST_TIME)
                true
            } else {
                false
            }
        }
    }

    fun setDay(day: Int) {
        sharedPref.edit().putInt(KEY_DAY, day).apply()
    }

    fun getDay(): Int {
        return sharedPref.getInt(KEY_DAY, 1)
    }

    private fun writeTime(time: String) {
        sharedPref.edit().putLong(time, System.currentTimeMillis()).apply()
    }

    private fun hasFirstTime(): Boolean {
        return sharedPref.contains("first_time")
    }

    companion object {
        private const val PREF_NAME = "daily"
        private const val KEY_DAY = "day"
        private const val KEY_FIRST_TIME = "first_time"

        private var PRIVATE_MODE = 0
    }
}