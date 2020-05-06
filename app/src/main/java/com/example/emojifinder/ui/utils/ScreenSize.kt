@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.example.emojifinder.ui.utils

import android.content.res.Configuration
import android.content.res.Resources
import com.example.emojifinder.R

object ScreenSize {

    fun getScreenSize(resources: Resources) : Float {
        return when {
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                resources.getDimension(R.dimen.emoji_huge_font)
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                resources.getDimension(R.dimen.emoji_medium_font)
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                resources.getDimension(R.dimen.emoji_small_font)
            }
            else -> resources.getDimension(R.dimen.emoji_font)
        }
    }
}