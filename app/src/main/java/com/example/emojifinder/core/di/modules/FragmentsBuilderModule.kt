package com.example.emojifinder.core.di.modules

import com.example.emojifinder.ui.account.AccountFragment
import com.example.emojifinder.ui.account.SettingsFragment
import com.example.emojifinder.ui.auth.login.LogInFragment
import com.example.emojifinder.ui.auth.registration.RegistrationFragment
import com.example.emojifinder.ui.auth.signin.SignInFragment
import com.example.emojifinder.ui.categories.CategoryGameFragment
import com.example.emojifinder.ui.game.GameFragment
import com.example.emojifinder.ui.main.MainMenuFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSingInFragment() : SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeLogInFragment() : LogInFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment() : RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeMainMenuFragment() : MainMenuFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoriesFragment() : CategoryGameFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment() : AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment() : SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeGameFragment() : GameFragment

}