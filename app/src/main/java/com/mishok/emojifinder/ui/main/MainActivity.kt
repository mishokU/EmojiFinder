package com.mishok.emojifinder.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.ActivityMainBinding
import com.mishok.emojifinder.domain.locale.ApplicationLanguageHelper
import com.mishok.emojifinder.domain.locale.LocaleHelper
import com.mishok.emojifinder.domain.payment.LOAD_PAYMENT_DATA_REQUEST_CODE
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.sounds.MediaPlayerPool
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import com.mishok.emojifinder.domain.viewModels.ShopViewModel
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    var TAG : String = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var navigation : NavController
    private lateinit var navGraph: NavGraph
    private lateinit var binding : ActivityMainBinding

    lateinit var mediaPlayerPool: MediaPlayerPool

    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop: ShopViewModel

    @Inject
    lateinit var viewModelFactoryUser: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    lateinit var randomEmojis : List<EmojiShopModel>
    var isVipAccount : Boolean = false

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
        loadUserMainData()
    }

    private fun loadUserMainData() {
        viewModel = injectViewModel(viewModelFactoryUser)
        viewModel.userMainDataResponse.observe(this, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        isVipAccount = it.data!!.vip
                    }
                }
            }
        })
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // value passed in AutoResolveHelper
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val paymentData = PaymentData.getFromIntent(data!!)
                        //handlePaymentSuccess(paymentData)
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(this,"Payment canceled", Toast.LENGTH_SHORT).show()
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        val status: Status? = AutoResolveHelper.getStatusFromIntent(data)
                        handleError(status?.statusCode)
                    }
                }
                // Re-enables the Google Pay payment button.
                //enableButtons()
            }
        }
    }

    private fun handleError(statusCode: Int?) {
        Toast.makeText(this, "Text: $statusCode", Toast.LENGTH_SHORT).show()
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

    //This we change the language
    override fun attachBaseContext(newBase: Context?) {
        val language = LocaleHelper.getLanguage(newBase!!)
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, language))
    }
}
