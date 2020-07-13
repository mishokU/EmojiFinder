package com.example.emojifinder.ui.application

import android.graphics.Color
import android.os.StrictMode
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.lifecycle.ViewModelProvider
import com.example.emojifinder.BuildConfig
import com.example.emojifinder.R
import com.example.emojifinder.core.di.DaggerAppComponent
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.domain.glide.MyAppGlideModule
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.util.*
import javax.inject.Inject


class MainApplication : DaggerApplication() {

    private val applicationInjector =
        DaggerAppComponent.builder().application(this).build()

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

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231", "FD9586430CDB7E0EF8DE82B129A21EE9")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()

        MobileAds.setRequestConfiguration(configuration)
        MobileAds.initialize(this) {}

        super.onCreate()
    }


    private fun initEmojies() {
        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs
        )

        val config: EmojiCompat.Config = FontRequestEmojiCompatConfig(this, fontRequest)
            .setReplaceAll(true)
            .setEmojiSpanIndicatorColor(Color.GREEN)
            .registerInitCallback(object : EmojiCompat.InitCallback() {
                override fun onInitialized() {
                    super.onInitialized()
                    Timer("init")
                }
            })
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