package com.example.emojifinder.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.example.emojifinder.R
import com.example.emojifinder.core.di.launcher.LaunchDestination
import com.example.emojifinder.core.di.launcher.LaunchViewModel
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.domain.result.EventObserver
import com.example.emojifinder.shared.utils.checkAllMatched
import com.google.common.graph.GraphBuilder
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        val viewModel: LaunchViewModel = injectViewModel(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY -> {
                    intent.putExtra("destination", R.id.accountAvatarFragment)
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