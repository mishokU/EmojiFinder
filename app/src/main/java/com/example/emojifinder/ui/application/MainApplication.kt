package com.example.emojifinder.ui.application

import android.graphics.Color
import android.os.StrictMode
import androidx.core.provider.FontRequest
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.example.emojifinder.BuildConfig
import com.example.emojifinder.R
import com.example.emojifinder.core.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MainApplication : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder().application(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return applicationInjector
    }

    override fun onCreate() {
        // ThreeTenBP for times and dates, called before super to be available for objects
        AndroidThreeTen.init(this)

        initEmojies()

        // Enable strict mode before Dagger creates graph
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        super.onCreate()
    }

    private fun initEmojies() {
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }


    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }
}