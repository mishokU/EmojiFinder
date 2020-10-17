package com.mishok.emojifinder.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.launcher.LaunchDestination
import com.mishok.emojifinder.core.di.launcher.LaunchViewModel
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.domain.result.EventObserver
import com.mishok.emojifinder.shared.utils.checkAllMatched
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUseCase()
    }

    private fun initUseCase() {
        val intent = Intent(this, MainActivity::class.java)
        val viewModel: LaunchViewModel = injectViewModel(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY -> {
                    intent.putExtra("destination", R.id.mainMenuFragment)
                    startActivity(intent)
                    finish()
                }
                LaunchDestination.SIGN_IN_ACTIVITY -> {
                    intent.putExtra("destination", R.id.signInFragment)
                    startActivity(intent)
                    finish()
                }
            }.checkAllMatched
        })
    }
}