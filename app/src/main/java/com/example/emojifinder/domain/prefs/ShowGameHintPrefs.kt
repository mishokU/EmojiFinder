package com.example.emojifinder.domain.prefs

import android.app.Application
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class ShowGameHintPrefs @Inject constructor(application: Application) {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "game hint"
    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun isHintShown() : Boolean {
        if(sharedPref.contains("hint")){
           return true
        }
        return false
    }

    fun isHintShown(shown: Boolean) {
        sharedPref.edit().putBoolean("hint", shown).apply()
    }
}