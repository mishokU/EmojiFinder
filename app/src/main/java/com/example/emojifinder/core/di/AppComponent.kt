package com.example.emojifinder.core.di

import android.app.Application
import android.content.Context
import com.example.emojifinder.core.di.modules.ActivityModule
import com.example.emojifinder.core.di.modules.AppModule
import com.example.emojifinder.ui.application.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
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