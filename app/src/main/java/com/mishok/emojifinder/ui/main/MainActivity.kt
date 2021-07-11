package com.mishok.emojifinder.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.wallet.PaymentData
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.ActivityMainBinding
import com.mishok.emojifinder.domain.locale.ApplicationLanguageHelper
import com.mishok.emojifinder.domain.locale.LocaleHelper
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.sounds.MediaPlayerPool
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(){

    var TAG : String = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var navigation : NavController
    private lateinit var navGraph: NavGraph
    private lateinit var binding : ActivityMainBinding

    lateinit var mediaPlayerPool: MediaPlayerPool

    @Inject
    lateinit var viewModelFactoryUser: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

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
        enableFullScreen()
        loadUserMainData()
    }

    private fun loadUserMainData() {
        viewModel = injectViewModel(viewModelFactoryUser)
        viewModel.userMainDataResponse.observe(this, {
            it?.let {
                when(it){
                    is Result.Success -> {
                        isVipAccount = it.data.vip
                    }
                    is Result.Error -> {}
                    is Result.Loading -> {}
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
        for (fragment in this.nav_host_fragment.childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()

            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
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
