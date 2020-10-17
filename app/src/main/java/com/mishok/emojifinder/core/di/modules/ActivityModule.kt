package com.mishok.emojifinder.core.di.modules

import com.mishok.emojifinder.ui.main.MainActivity
import com.mishok.emojifinder.ui.main.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashScreen(): SplashActivity
}