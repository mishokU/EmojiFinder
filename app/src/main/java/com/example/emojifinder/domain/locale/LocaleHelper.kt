@file:Suppress("DEPRECATION")

package com.example.emojifinder.domain.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*


object LocaleHelper {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "language"
    private val PREF_KEY = PREF_NAME

    fun getLanguage(context : Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        return prefs.getString(PREF_KEY, "en")!!
    }

    private fun saveLanguage(context : Context, language: String?) {
        val prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        prefs.edit().putString(PREF_KEY, language).apply()
    }

    fun setLocale(context: Context, language: String) {
        saveLanguage(context, language)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    private fun updateResources(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    private fun updateResourcesLegacy(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}