package com.example.emojifinder.core.di.modules

import com.example.emojifinder.ui.account.AccountAvatarFragment
import com.example.emojifinder.ui.account.MainAccountInfoFragment
import com.example.emojifinder.ui.account.AccountFragment
import com.example.emojifinder.ui.settings.SettingsFragment
import com.example.emojifinder.ui.auth.login.ForgetPasswordFragment
import com.example.emojifinder.ui.auth.login.LogInFragment
import com.example.emojifinder.ui.auth.registration.RegistrationFragment
import com.example.emojifinder.ui.auth.signin.SignInFragment
import com.example.emojifinder.ui.boxes.LootBoxesFragment
import com.example.emojifinder.ui.categories.CategoryGameFragment
import com.example.emojifinder.ui.constructor.LevelConstructorFragment
import com.example.emojifinder.ui.daily.DailyWinningsFragment
import com.example.emojifinder.ui.game.arcade.ArcadeGameFragment
import com.example.emojifinder.ui.game.campaign.gameAlerts.GameFragment
import com.example.emojifinder.ui.help.HelpFragment
import com.example.emojifinder.ui.help.ReadQAFragment
import com.example.emojifinder.ui.help.WriteQuestionFragment
import com.example.emojifinder.ui.levels.YourLevelsFragment
import com.example.emojifinder.ui.localization.LocalizeAppFragment
import com.example.emojifinder.ui.main.MainMenuFragment
import com.example.emojifinder.ui.rating.RatingFragment
import com.example.emojifinder.ui.settings.LogOutFragment
import com.example.emojifinder.ui.shop.ShopFragment
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
    abstract fun contributeSettingsFragment() : SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeGameFragment() : GameFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment() : AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeMainAccountInfoFragment() : MainAccountInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountAvatarFragment() : AccountAvatarFragment

    @ContributesAndroidInjector
    abstract fun contributeLootBoxesFragment() : LootBoxesFragment

    @ContributesAndroidInjector
    abstract fun contributeLocalizeFragment() : LocalizeAppFragment

    @ContributesAndroidInjector
    abstract fun contributeHelpFragment() : HelpFragment

    @ContributesAndroidInjector
    abstract fun contributeReadQAFragment() : ReadQAFragment

    @ContributesAndroidInjector
    abstract fun contributeWriteQuestionFragment() : WriteQuestionFragment

    @ContributesAndroidInjector
    abstract fun contributeForgetPasswordFragment() : ForgetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeLevelConstructorFragment() : LevelConstructorFragment

    @ContributesAndroidInjector
    abstract fun contributeUserLevelsFragment() : YourLevelsFragment

    @ContributesAndroidInjector
    abstract fun contributeArcadeGameFragment() : ArcadeGameFragment

    @ContributesAndroidInjector
    abstract fun contributeRatingFragment() : RatingFragment

    @ContributesAndroidInjector
    abstract fun contributeShopFragment() : ShopFragment

    @ContributesAndroidInjector
    abstract fun contributeDailyFragment() : DailyWinningsFragment

    @ContributesAndroidInjector
    abstract fun contributeLogOutFragment() : LogOutFragment

}