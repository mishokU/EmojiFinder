package com.example.emojifinder.ui.shop

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.databinding.FragmentShopBinding
import com.example.emojifinder.domain.adds.MyRewardedVideoListener
import com.example.emojifinder.domain.adds.REWARDED_VIDEO_ID
import com.example.emojifinder.domain.payment.*
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.*
import dagger.android.support.DaggerFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.Optional.empty
import javax.inject.Inject
import kotlin.math.roundToInt


class ShopFragment : DaggerFragment() {

    private lateinit var binding : FragmentShopBinding
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private val googlePaymentsClient: PaymentsClient by lazy {
        Wallet.getPaymentsClient(
            this.requireActivity(),
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build()
        )
    }

    private var paymentsClient: PaymentsClient? = null


    private val selectedGarment: JSONObject? = null
    private val garmentList: JSONArray? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    lateinit var values : AccountValuesModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)

        paymentsClient = PaymentsUtil.createPaymentsClient(this.requireActivity());
        possiblyShowGooglePayButton();

        initEmojis()
        initAccountValues()
        initRewardedAd()
        initCountyCost()
        addButtons()

        return binding.root
    }

    private fun initCountyCost() {
//        val format: NumberFormat = NumberFormat.getCurrencyInstance()
//        binding.starterPackBtn.text = format.format(format)

    }

    private fun initEmojis() {
        binding.emosEt.setText(resources.getString(R.string.emoji_emos))
        binding.boxEt.setText(resources.getString(R.string.emoji_ticket))

        binding.mediumBoxEt.setText(resources.getString(R.string.emoji_ticket))
        binding.mediumEmosEt.setText(resources.getString(R.string.emoji_emos))

        binding.noAddsEmoji.setText("⛔")
        binding.vipEmosEmoji.setText(resources.getString(R.string.emoji_emos))
        binding.vipEmoji.setText("\uD83D\uDE00")
    }

    private fun initAccountValues() {
        viewModel.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success ->{
                        values = it.data
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addButtons() {
        binding.chestForAdd.setOnClickListener {
            if(mRewardedVideoAd.isLoaded){
                mRewardedVideoAd.show()
            }
        }

        binding.starterPackBtn.setOnClickListener {
            requestPayment(STARTER_PACK_PRICE)
        }
        binding.mediumPackBtn.setOnClickListener {
            requestPayment(MEDIUM_PACK_PRICE)
        }
        binding.vipPackBtn.setOnClickListener {
            requestPayment(VIP_PACK_PRICE)
        }
    }

    private fun createPaymentsClient(activity: Activity): PaymentsClient? {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(Constants.PAYMENTS_ENVIRONMENT)
            .setTheme(WalletConstants.THEME_LIGHT)
            .build()

        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getIsReadyToPayRequest(): Optional<JSONObject> {
        return try {
            val isReadyToPayRequest = getBaseRequest().apply {
                put("allowedPaymentMethods", JSONArray()
                    .put(getBaseCardPaymentMethod()))
            }
            Optional.of(isReadyToPayRequest)
        } catch (e: JSONException) {
            empty()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun possiblyShowGooglePayButton() {
        val isReadyToPayJson: Optional<JSONObject> = getIsReadyToPayRequest()
        if (!isReadyToPayJson.isPresent) {
            return
        }

        val request: IsReadyToPayRequest = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val taskIsReady: Task<Boolean> = googlePaymentsClient.isReadyToPay(request)
        taskIsReady.addOnCompleteListener(this.requireActivity()) { task ->
            if (task.isSuccessful) {
                //setGooglePayAvailable(task.result)
            } else {
                Log.w("isReadyToPay failed", task.exception)
            }
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (!available) {
            binding.starterPackBtn.isEnabled = false
            binding.mediumPackBtn.isEnabled = false
            binding.vipPackBtn.isEnabled = false
        } else {
            Toast.makeText(this.requireContext(), R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestPayment(price : String) {

        // Disables the button to prevent multiple clicks.
        //disableButtons()

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        try {
            val garmentPrice = price.toDouble()
            val garmentPriceCents = (garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.toLong()).roundToInt()
            val priceCents: Long = garmentPriceCents.toLong()

            val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
            if (!paymentDataRequestJson.isPresent) {
                return
            }
            val request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString())

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                    paymentsClient!!.loadPaymentData(request),
                    this.requireActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE
                )
            }
        } catch (e: JSONException) {
            throw RuntimeException("The price cannot be deserialized from the JSON object.")
        }
    }

    private fun handleError(statusCode: Int?) {

    }

    private fun enableButtons() {
        binding.vipPackBtn.isClickable = true
        binding.mediumPackBtn.isClickable = true
        binding.starterPackBtn.isClickable = true
    }

    private fun handlePaymentSuccess(paymentData: PaymentData?) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        val paymentInfo = paymentData!!.toJson() ?: return

        try {
            val paymentMethodData = JSONObject(paymentInfo).getJSONObject("paymentMethodData")
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            val tokenizationData = paymentMethodData.getJSONObject("tokenizationData")
            val tokenizationType = tokenizationData.getString("type")
            val token = tokenizationData.getString("token")

            if ("PAYMENT_GATEWAY" == tokenizationType && "examplePaymentMethodToken" == token) {
                AlertDialog.Builder(this.requireContext())
                    .setTitle("Warning")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }

            val info = paymentMethodData.getJSONObject("info")
            val billingName = info.getJSONObject("billingAddress").getString("name")
            //Toast.makeText(this.requireContext(), getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()
            // Logging token string.
            Log.d("Google Pay token: ", token)

        } catch (e: JSONException) {
            throw java.lang.RuntimeException("The selected garment cannot be parsed from the list of elements")
        }
    }

    private fun disableButtons() {
        binding.vipPackBtn.isClickable = false
        binding.mediumPackBtn.isClickable = false
        binding.starterPackBtn.isClickable = false
    }

    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(requireContext())
    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(requireContext())
    }

    private fun initRewardedAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(requireContext())
        mRewardedVideoAd.rewardedVideoAdListener = object : MyRewardedVideoListener() {
            override fun onRewarded(p0: RewardItem?) {
                viewModel.updateUserBoxes(values.boxes + 1)
            }
            override fun onRewardedVideoAdLoaded() {
                binding.chestForAdd.isEnabled = true
            }
            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
                binding.chestForAdd.isEnabled = false
            }
        }

        mRewardedVideoAd.loadAd(REWARDED_VIDEO_ID, AdRequest.Builder().build())
    }
}
