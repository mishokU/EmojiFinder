package com.example.emojifinder.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.ActivityMainBinding
import com.example.emojifinder.domain.payment.LOAD_PAYMENT_DATA_REQUEST_CODE
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MediaPlayerPool
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
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

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop: ShopViewModel

    lateinit var randomEmojis : List<EmojiShopModel>


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
        enableFullScreen()

        loadRandomEmojis()
    }

    private fun loadRandomEmojis() {
        viewModelShop = injectViewModel(viewModelFactory)
        viewModelShop.emojisResponse.observe(this, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        randomEmojis = it.data
                    }
                }
            }
        })
    }

    private fun enableFullScreen() {
        val w: Window = window // in Activity's onCreate() for instance
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data == null)
                            return

                        val paymentData = PaymentData.getFromIntent(data)
                    }
                    Activity.RESULT_CANCELED -> {
                        // Пользователь нажал назад,
                        // когда был показан диалог google pay
                        // если показывали загрузку или что-то еще,
                        // можете отменить здесь
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        if (data == null)
                            return

                        // Гугл сам покажет диалог ошибки.
                        // Можете вывести логи и спрятать загрузку,
                        // если показывали
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.e("GOOGLE PAY", "Load payment data has failed with status: $status")
                    }
                    else -> { }
                }
            }
            else -> { }
        }
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
