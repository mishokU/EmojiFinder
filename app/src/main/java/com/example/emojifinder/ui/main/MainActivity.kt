package com.example.emojifinder.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.ActivityMainBinding
import com.example.emojifinder.domain.sounds.MediaPlayerPool
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var navigation : NavController
    private lateinit var navGraph: NavGraph
    private lateinit var binding : ActivityMainBinding

    lateinit var mediaPlayerPool: MediaPlayerPool

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater

        navGraph = graphInflater.inflate(R.navigation.navigation)
        navigation = navHostFragment.navController
        navGraph.startDestination = intent.getIntExtra("destination", R.id.signInFragment)
        navigation.graph = navGraph

        initMediaPlayer()
        playBackgroundMusic()
    }

    fun navigateFirstTabWithClearStack() {
        navGraph.startDestination = R.id.signInFragment
        navigation.graph = navGraph
    }

    private fun initMediaPlayer() {
        mediaPlayerPool = MediaPlayerPool(this.application)
    }

    private fun playBackgroundMusic() {
        mediaPlayerPool.playBackground()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerPool.pauseBackground()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayerPool.resumeBackground()
    }
}
