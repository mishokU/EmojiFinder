package com.example.emojifinder.core.di.modules

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.provider.MediaStore
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.ui.application.MainApplication
import com.example.emojifinder.data.prefs.PreferenceStorage
import com.example.emojifinder.data.prefs.SharedPreferenceStorage
import com.example.emojifinder.core.di.utils.CoroutineScopeMain
import com.example.emojifinder.data.db.local.emoji_json.ShopEmojiService
import com.example.emojifinder.data.db.local.fake.LevelConstructorService
import com.example.emojifinder.data.db.local.room.database.LevelsDatabase
import com.example.emojifinder.data.db.remote.api.FirebaseLevels
import com.example.emojifinder.data.db.remote.api.FirebaseRegistration
import com.example.emojifinder.data.db.remote.service.*
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.domain.prefs.ShowGameHintPrefs
import com.example.emojifinder.domain.sounds.MediaPlayerPool
import com.example.emojifinder.domain.user.FirebaseCreateUserAccount
import com.example.emojifinder.domain.user.FirebaseCreateUserAccountImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [ViewModelsModule::class])
class AppModule {

    @Provides
    fun provideContext(application: MainApplication) : Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideFirebaseUserData() : FirebaseUserData = FirebaseUserData()

    @Singleton
    @Provides
    fun provideFirebaseAuthHandler() : FirebaseAuthHandler =
        FirebaseAuthHandler()

    @Singleton
    @Provides
    fun provideFirebaseRegistration(firebaseCreateUserAccount: FirebaseCreateUserAccount)
            : FirebaseRegistration =
        FirebaseRegistrationImpl(
            firebaseCreateUserAccount
        )

    @Singleton
    @Provides
    fun provideFirebaseStatistics() : FirebaseLevelStatisticImpl = FirebaseLevelStatisticImpl()

    @Singleton
    @Provides
    fun provideFirebaseCategories() : FirebaseLevels = FirebaseLevelsImpl()

    @Singleton
    @Provides
    fun provideFirebaseCollection() : FirebaseCollection = FirebaseCollection()

    @Singleton
    @Provides
    fun provideCreateUserAccount(collection: FirebaseCollection)
            : FirebaseCreateUserAccount = FirebaseCreateUserAccountImpl(collection)

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Provides
    fun providesWifiManager(context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun providesConnectivityManager(context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    @Provides
    fun providesClipboardManager(context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

    @Singleton
    @Provides
    fun provideShopEmojiService(application : Application)
            : ShopEmojiService = ShopEmojiService(application)

    @Singleton
    @Provides
    fun provideGameSharedPref(application: Application)
            : ShowGameHintPrefs = ShowGameHintPrefs(application)

    @Singleton
    @Provides
    fun provideSettingsSharedPref(application: Application)
            : SettingsPrefs = SettingsPrefs(application)

    @Singleton
    @Provides
    fun provideLevelConstructorService() : LevelConstructorService = LevelConstructorService()

    @Singleton
    @Provides
    fun provideMediaPlayerPool(application: Application) : MediaPlayerPool = MediaPlayerPool(application)

    @Singleton
    @Provides
    fun provideLevelsDatabase(application: Application) :
            LevelsDatabase = LevelsDatabase.getDatabase(application.applicationContext)

    @Singleton
    @Provides
    fun provideFirebaseUsers() : FirebaseUsers = FirebaseUsers()

    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @CoroutineScopeMain
    @Provides
    fun provideCoroutineScopeMain() = CoroutineScope(Dispatchers.Main)
}