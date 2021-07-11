package com.mishok.emojifinder.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.data.db.local.models.Daily
import com.mishok.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.mishok.emojifinder.databinding.FragmentDailyWinningsBinding
import com.mishok.emojifinder.domain.adds.MyRewardedVideoListener
import com.mishok.emojifinder.domain.adds.REWARDED_VIDEO_ID
import com.mishok.emojifinder.domain.prefs.DailyWinningsPrefs
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import com.mishok.emojifinder.domain.viewModels.DailyViewModel
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import com.mishok.emojifinder.ui.utils.ScreenSize
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class DailyWinningsFragment : DaggerFragment() {


    private lateinit var binding: FragmentDailyWinningsBinding
    private lateinit var adapter: DailyRecyclerViewAdapter
    private lateinit var day: DailyUI
    private lateinit var values: AccountValuesModel
    private lateinit var mRewardedVideoAd: RewardedVideoAd

    private var emojis: MutableList<EmojiAppCompatEditText> = mutableListOf()
    private var shopEmojis: MutableList<EmojiShopModel> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DailyViewModel

    @Inject
    lateinit var viewModelFactoryValues: ViewModelProvider.Factory
    lateinit var viewModelValues: AccountViewModel

    @Inject
    lateinit var daily: DailyWinningsPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyWinningsBinding.inflate(inflater)

        fetchUserValues()

        getDay()
        initEmojis()
        loadAllEmojis()
        initDailyAdapter()

        startAdClaimBtn()

        fetchDailies()

        initClaimButton()
        initRewarded()

        return binding.root
    }

    private fun loadAllEmojis() {
        for (i: Int in 0..3) {
            shopEmojis.add(viewModelValues.randomEmojis.random())
        }
        binding.firstDailyEmoji.setText(shopEmojis[0].text)
        binding.secondDailyEmoji.setText(shopEmojis[1].text)
        binding.thirdDailyEmoji.setText(shopEmojis[2].text)
        binding.fourthDailyEmoji.setText(shopEmojis[3].text)
    }

    private fun initEmojis() {
        emojis.add(binding.firstDailyEmoji)
        emojis.add(binding.secondDailyEmoji)
        emojis.add(binding.thirdDailyEmoji)
        emojis.add(binding.fourthDailyEmoji)
    }

    private fun startAdClaimBtn() {
        val animation = RotateAnimation(
            -5f, 5f,
            ScreenSize.dipToPixels(requireContext(), 100f),
            ScreenSize.dipToPixels(requireContext(), 30f)
        )

        animation.repeatMode = Animation.REVERSE
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator()
        animation.duration = 400L

        binding.doubleClaimBtn.animation = animation
    }

    private fun initRewarded() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(requireContext())
        mRewardedVideoAd.rewardedVideoAdListener = object : MyRewardedVideoListener() {
            override fun onRewarded(p0: RewardItem?) {
                increaseUserValues(2)
            }

            override fun onRewardedVideoAdClosed() {
                super.onRewardedVideoAdClosed()
                findNavController().navigate(R.id.mainMenuFragment)
            }
        }

        mRewardedVideoAd.loadAd(
            REWARDED_VIDEO_ID,
            AdRequest.Builder().build()
        )
    }

    private fun increaseUserValues(multiplier: Int) {
        val item = adapter.items[daily.getDay() - 2]
        when (item.type) {
            Daily.EMOS -> {
                viewModelValues.updateUserEmos(values.emos + item.cost * multiplier)
            }
            Daily.BOX -> {
                viewModelValues.updateUserBoxes(values.boxes + item.cost * multiplier)
            }
            Daily.EMOJI -> {
                binding.emojisDailyPlace.visibility = View.VISIBLE
                for (i: Int in 0 until item.cost * multiplier) {
                    emojis[i].visibility = View.VISIBLE
                    emojis[i].setText(shopEmojis[i].text)
                    viewModelValues.addGeneratedEmoji(shopEmojis[i])
                }
                viewModelValues.updateUserEmojis(values.emojis + item.cost * multiplier)
            }
        }

    }

    private fun getDay() {
        day = DailyUI(daily.getDay())
        binding.daysTv.text = "Day " + day.day.toString()
    }

    private fun fetchUserValues() {
        viewModelValues = injectViewModel(viewModelFactoryValues)
        viewModelValues.fetchUserValues()
        viewModelValues.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Loading -> {
                        binding.loadingValues1.visibility = View.VISIBLE
                        binding.loadingValues2.visibility = View.VISIBLE
                        binding.loadingValues3.visibility = View.VISIBLE
                        binding.userValuesError.visibility = View.GONE
                        binding.valuesPlace.visibility = View.INVISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingValues1.visibility = View.GONE
                        binding.loadingValues2.visibility = View.GONE
                        binding.loadingValues3.visibility = View.GONE
                        binding.userValuesError.visibility = View.GONE
                        binding.valuesPlace.visibility = View.VISIBLE
                        binding.values = it.data
                        values = it.data
                    }
                    is Result.Error -> {
                        binding.loadingValues1.visibility = View.GONE
                        binding.loadingValues2.visibility = View.GONE
                        binding.loadingValues3.visibility = View.GONE
                        binding.valuesPlace.visibility = View.GONE
                        binding.userValuesError.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun fetchDailies() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.dailies.observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        adapter.items = it.data
                    }
                    is Result.Error -> {}
                    is Result.Loading -> {}
                }
            }
        })
    }

    private fun initDailyAdapter() {
        adapter = DailyRecyclerViewAdapter(daily.getDay())
        binding.dailyWinningsRv.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(requireContext())
    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(requireContext())
    }

    private fun initClaimButton() {
        binding.claimBtn.setOnClickListener {
            var day = binding.daysTv.text.drop(4).toString().toInt()
            if (day >= 15) day = 2 else ++day
            daily.setDay(day)
            increaseUserValues(1)
            findNavController().navigate(R.id.mainMenuFragment)
        }
        binding.doubleClaimBtn.setOnClickListener {
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
            }
        }
    }
}
