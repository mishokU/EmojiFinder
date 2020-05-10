package com.example.emojifinder.core.di.modules

import com.example.emojifinder.ui.account.AccountFragment
import com.example.emojifinder.ui.account.MainAccountInfoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountModule {

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment() : AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeMainAccountInfoFragment() : MainAccountInfoFragment

}