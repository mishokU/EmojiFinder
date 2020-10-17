package com.mishok.emojifinder.core.di

import android.app.Application
import com.mishok.emojifinder.core.di.modules.ActivityModule
import com.mishok.emojifinder.core.di.modules.AppModule
import com.mishok.emojifinder.ui.application.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build() : AppComponent
    }

    override fun inject(application : MainApplication)
}