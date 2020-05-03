package com.example.emojifinder.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.emojifinder.R
import com.example.emojifinder.core.di.launcher.LaunchDestination
import com.example.emojifinder.core.di.launcher.LaunchViewModel
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.ActivityMainBinding
import com.example.emojifinder.domain.result.EventObserver
import com.example.emojifinder.shared.utils.checkAllMatched
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var navigation : NavController
    private lateinit var navGraph: NavGraph

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater

        navGraph = graphInflater.inflate(R.navigation.navigation)
        navigation = navHostFragment.navController
        navGraph.startDestination = intent.getIntExtra("destination", R.id.signInFragment)
        navigation.graph = navGraph
    }


}