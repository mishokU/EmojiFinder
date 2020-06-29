package com.example.emojifinder.ui.daily

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
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
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.local.models.Daily
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.databinding.FragmentDailyWinningsBinding
import com.example.emojifinder.domain.adds.MyRewardedVideoListener
import com.example.emojifinder.domain.adds.REWARDED_VIDEO_ID
import com.example.emojifinder.domain.prefs.DailyWinningsPrefs
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.DailyViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.utils.ScreenSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class DailyWinningsFragment : DaggerFragment() {

    private lateinit var binding : FragmentDailyWinningsBinding
    private lateinit var adapter : DailyRecyclerViewAdapter
    private lateinit var day : DailyUI
    private lateinit var values : AccountValuesModel
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private var emojis : MutableList<EmojiAppCompatEditText> = mutableListOf()
    private var shopEmojis : MutableList<EmojiShopModel> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : DailyViewModel

    @Inject
    lateinit var viewModelFactoryValues : ViewModelProvider.Factory
    lateinit var viewModelValues : AccountViewModel

    @Inject
    lateinit var viewModelFactoryShop : ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    @Inject
    lateinit var daily : DailyWinningsPrefs


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDailyWinningsBinding.inflate(inflater)

        getDay()

        initEmojis()

        loadAllEmojis()
        initValues()
        initDailyAdapter()
        startAdClaimBtn()

        fetchDailies()
        fetchUserValues()
        initClaimButton()

        initRewarded()

        return binding.root
    }

    private fun loadAllEmojis() {
        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModelShop.loadDailyEmojisFromJson()
        viewModelShop.emojisDailyResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success -> {
                        for(i : Int in 0..3){
                            shopEmojis.add(it.data.random()!!)
                        }
                        binding.firstDailyEmoji.setText(shopEmojis[0].text)
                        binding.secondDailyEmoji.setText(shopEmojis[1].text)
                        binding.thirdDailyEmoji.setText(shopEmojis[2].text)
                        binding.fourthDailyEmoji.setText(shopEmojis[3].text)
                    }
                }
            }
        })
    }

    private fun initEmojis() {
        emojis.add(binding.firstDailyEmoji)
        emojis.add(binding.secondDailyEmoji)
        emojis.add(binding.thirdDailyEmoji)
        emojis.add(binding.fourthDailyEmoji)
    }

    private fun startAdClaimBtn() {
        val animation = RotateAnimation(-5f, 5f,
            ScreenSize.dipToPixels(requireContext(), 100f),
            ScreenSize.dipToPixels(requireContext(), 30f))

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
        }

        mRewardedVideoAd.loadAd(
            REWARDED_VIDEO_ID,
            AdRequest.Builder().build())
    }

    private fun increaseUserValues(multiplier : Int) {
        val item = adapter.currentList[daily.getDay() - 2]
        when(item.type){
            Daily.EMOS -> {
                viewModelValues.updateUserEmos(values.emos + item.cost * multiplier)
                this.findNavController().navigate(R.id.mainMenuFragment)
            }
            Daily.BOX -> {
                viewModelValues.updateUserBoxes(values.boxes + item.cost * multiplier)
                this.findNavController().navigate(R.id.mainMenuFragment)
            }
            Daily.EMOJI ->  {
                binding.emojisDailyPlace.visibility = View.VISIBLE
                for(i : Int in 0 until item.cost * multiplier){
                    emojis[i].visibility = View.VISIBLE
                    emojis[i].setText(shopEmojis[i].text)
                    viewModelValues.addGeneratedEmoji(shopEmojis[i])
                }
                viewModelValues.updateUserEmojis(values.emojis + item.cost * multiplier)

                Handler().postDelayed({
                    this.findNavController().navigate(R.id.mainMenuFragment)
                }, 3000)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDay() {
        day = DailyWinningsFragmentArgs.fromBundle(requireArguments()).Daily
        binding.daysTv.text = "Day " + day.day.toString()
    }

    private fun fetchUserValues() {
        viewModelValues = injectViewModel(viewModelFactoryValues)
        viewModelValues.fetchUserValues()
        viewModelValues.userValuesResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.loadingValues1.visibility = View.VISIBLE
                        binding.loadingValues2.visibility = View.VISIBLE
                        binding.loadingValues3.visibility = View.VISIBLE
                        binding.valuesPlace.visibility = View.INVISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingValues1.visibility = View.GONE
                        binding.loadingValues2.visibility = View.GONE
                        binding.loadingValues3.visibility = View.GONE
                        binding.valuesPlace.visibility = View.VISIBLE
                        values = it.data
                        binding.values = it.data
                    }
                }
            }
        })
    }

    private fun initValues() {
        binding.emojiBoxEt.setText("\uD83C\uDF81")
        binding.emosEt.setText("\uD83D\uDCB0")
        binding.emojiEt.setText("\uD83D\uDE00")
    }

    private fun fetchDailies() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.dailies.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Success ->{
                        adapter.setDay(day.day)
                        adapter.submitList(it.data)
                    }
                }
            }
        })
    }

    private fun initDailyAdapter() {
        adapter = DailyRecyclerViewAdapter()
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
            if(day >= 15){
                day = 2
            } else {
                ++day
            }
            daily.setDay(day)
            increaseUserValues(1)
        }

        binding.doubleClaimBtn.setOnClickListener {
            if(mRewardedVideoAd.isLoaded){
                mRewardedVideoAd.show()
            }
        }

    }


}
