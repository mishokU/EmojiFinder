package com.example.emojifinder.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentsClient
import dagger.android.support.DaggerFragment
import org.json.JSONObject
import javax.inject.Inject


class ShopFragment : DaggerFragment() {

    private lateinit var binding : FragmentShopBinding
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private lateinit var googlePaymentsClient: PaymentsClient

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    lateinit var values : AccountValuesModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater)

        googlePaymentsClient = GooglePaymentUtils.createGoogleApiClientForPay(requireContext())

        viewModel = injectViewModel(viewModelFactory)

        initAccountValues()

        initRewardedAd()
        addListenerToAdView()
        addButtons()
        return binding.root
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

    private fun addButtons() {
        binding.vipButton.setOnClickListener {

        }
        binding.chestForAdd.setOnClickListener {
            if(mRewardedVideoAd.isLoaded){
                mRewardedVideoAd.show()
            }
        }

        binding.starterPackBtn.setOnClickListener {
            createTransactionInfo(STARTER_PACK_PRICE)
        }
        binding.mediumPackBtn.setOnClickListener {
            createTransactionInfo(MEDIUM_PACK_PRICE)
        }
        binding.hugePackBtn.setOnClickListener {
            createTransactionInfo(HUGE_PACK_PRICE)
        }
        binding.firstEmoBtn.setOnClickListener {
            createTransactionInfo(FIRST_EMO_PRICE)
        }

        binding.secondEmoBtn.setOnClickListener {
            createTransactionInfo(SECOND_EMO_PRICE)
        }

        binding.thirdEmoBtn.setOnClickListener {
            createTransactionInfo(THIRD_EMO_PRICE)
        }
    }

    private fun createTransactionInfo(price: String) {
        val request = GooglePaymentUtils.createPaymentDataRequest(price)
        AutoResolveHelper.resolveTask(googlePaymentsClient.loadPaymentData(request),
            requireActivity(),
            LOAD_PAYMENT_DATA_REQUEST_CODE)
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
        }

        mRewardedVideoAd.loadAd(
            REWARDED_VIDEO_ID,
            AdRequest.Builder().build())
    }

    private fun addListenerToAdView() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdClosed() {
                binding.adView.loadAd(AdRequest.Builder().build())
            }
        }
    }

}
