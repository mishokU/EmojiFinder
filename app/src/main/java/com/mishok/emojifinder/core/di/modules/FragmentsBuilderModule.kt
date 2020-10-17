package com.mishok.emojifinder.core.di.modules

import com.mishok.emojifinder.ui.account.AccountAvatarFragment
import com.mishok.emojifinder.ui.account.AccountFragment
import com.mishok.emojifinder.ui.account.MainAccountInfoFragment
import com.mishok.emojifinder.ui.auth.login.ForgetPasswordFragment
import com.mishok.emojifinder.ui.auth.login.LogInFragment
import com.mishok.emojifinder.ui.auth.registration.RegistrationFragment
import com.mishok.emojifinder.ui.auth.signin.SignInFragment
import com.mishok.emojifinder.ui.boxes.LootBoxesFragment
import com.mishok.emojifinder.ui.categories.CategoryGameFragment
import com.mishok.emojifinder.ui.constructor.LevelConstructorFragment
import com.mishok.emojifinder.ui.daily.DailyWinningsFragment
import com.mishok.emojifinder.ui.game.arcade.ArcadeGameFragment
import com.mishok.emojifinder.ui.game.campaign.gameAlerts.GameFragment
import com.mishok.emojifinder.ui.help.HelpFragment
import com.mishok.emojifinder.ui.help.ReadQAFragment
import com.mishok.emojifinder.ui.help.WriteQuestionFragment
import com.mishok.emojifinder.ui.levels.YourLevelsFragment
import com.mishok.emojifinder.ui.localization.LocalizeAppFragment
import com.mishok.emojifinder.ui.main.MainMenuFragment
import com.mishok.emojifinder.ui.rating.RatingFragment
import com.mishok.emojifinder.ui.settings.LogOutFragment
import com.mishok.emojifinder.ui.settings.SettingsFragment
import com.mishok.emojifinder.ui.shop.ShopFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeSingInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeLogInFragment(): LogInFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeMainMenuFragment(): MainMenuFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoriesFragment(): CategoryGameFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeGameFragment(): GameFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeMainAccountInfoFragment(): MainAccountInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountAvatarFragment(): AccountAvatarFragment

    @ContributesAndroidInjector
    abstract fun contributeLootBoxesFragment(): LootBoxesFragment

    @ContributesAndroidInjector
    abstract fun contributeLocalizeFragment(): LocalizeAppFragment

    @ContributesAndroidInjector
    abstract fun contributeHelpFragment(): HelpFragment

    @ContributesAndroidInjector
    abstract fun contributeReadQAFragment(): ReadQAFragment

    @ContributesAndroidInjector
    abstract fun contributeWriteQuestionFragment(): WriteQuestionFragment

    @ContributesAndroidInjector
    abstract fun contributeForgetPasswordFragment(): ForgetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeLevelConstructorFragment(): LevelConstructorFragment

    @ContributesAndroidInjector
    abstract fun contributeUserLevelsFragment(): YourLevelsFragment

    @ContributesAndroidInjector
    abstract fun contributeArcadeGameFragment(): ArcadeGameFragment

    @ContributesAndroidInjector
    abstract fun contributeRatingFragment(): RatingFragment

    @ContributesAndroidInjector
    abstract fun contributeShopFragment(): ShopFragment

    @ContributesAndroidInjector
    abstract fun contributeDailyFragment(): DailyWinningsFragment

    @ContributesAndroidInjector
    abstract fun contributeLogOutFragment(): LogOutFragment

}