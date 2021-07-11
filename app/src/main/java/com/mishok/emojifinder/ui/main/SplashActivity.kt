package com.mishok.emojifinder.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.launchers.LaunchDailyViewModel
import com.mishok.emojifinder.core.di.launchers.LaunchViewModel
import com.mishok.emojifinder.core.di.utils.LaunchDestination
import com.mishok.emojifinder.core.di.utils.LaunchFragmentDestination
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.domain.result.EventObserver
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.ShopViewModel
import com.mishok.emojifinder.shared.utils.checkAllMatched
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewDailyModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop: ShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRandomEmojis()
    }

    private fun initUseCase() {
        val intent = Intent(this, MainActivity::class.java)
        val viewModel: LaunchViewModel = injectViewModel(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY -> {
                    initDailyUseCase(intent)
                }
                LaunchDestination.SIGN_IN_ACTIVITY -> {
                    intent.putExtra("destination", R.id.signInFragment)
                    startActivity(intent)
                    finish()
                }
            }.checkAllMatched
        })
    }

    private fun initDailyUseCase(intent: Intent) {
        val dailyViewModelLauncher: LaunchDailyViewModel = injectViewModel(viewDailyModelFactory)
        dailyViewModelLauncher.launchDestination.observe(this, EventObserver {destinationFragment ->
            when(destinationFragment){
                LaunchFragmentDestination.DAILY_FRAGMENT -> {
                    intent.putExtra("destination", R.id.dailyWinningsFragment)
                }
                LaunchFragmentDestination.MAIN_MENU_FRAGMENT -> {
                    intent.putExtra("destination", R.id.mainMenuFragment)
                }
            }
            startActivity(intent)
            finish()
        })
    }

    private fun loadRandomEmojis() {
        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModelShop.emojisResponse.observe(this, {
            it?.let {
                when(it){
                    is Result.Success -> {
                        viewModelShop.setRandomEmojis(it.data)
                        initUseCase()
                    }
                    is Result.Error -> {
                        Toast.makeText(this, R.string.an_error_has_occurred, Toast.LENGTH_SHORT).show()
                    }
                    Result.Loading -> {
                        Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}