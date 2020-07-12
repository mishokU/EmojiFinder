package com.example.emojifinder.ui.boxes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.databinding.FragmentLootBoxesBinding
import com.example.emojifinder.domain.adds.MyRewardedVideoListener
import com.example.emojifinder.domain.adds.REWARDED_VIDEO_ID
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.sounds.MusicType
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random


class LootBoxesFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop: ShopViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    lateinit var binding: FragmentLootBoxesBinding
    lateinit var adapter: LootBoxRecyclerViewAdapter
    lateinit var values: AccountValuesModel

    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private var emojis: MutableList<EmojiAppCompatEditText> = mutableListOf()
    private lateinit var rotateAnimator: RotateAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLootBoxesBinding.inflate(inflater)

        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModel = injectViewModel(viewModelFactory)

        binding.lifecycleOwner = this

        getValuesFromBundle()

        initRouletteAnimation()
        initAdapter()
        initRouletteEmojis()
        initShopViewModel()

        initValues()
        initBuyChestButton()
        initWatchAdButton()

        initRewardedAd()


        return binding.root
    }

    private fun initValues() {
        binding.emojiBoxEt.setText("\uD83C\uDF9F️")
        binding.emosEt.setText("\uD83D\uDCB0")
        binding.emojiEt.setText("\uD83D\uDE00")
        binding.rollWheelBtn.text = "\uD83D\uDCB0" + " 200"
        binding.rollTicket.text = "\uD83C\uDF9F️" + " 1"
    }

    private fun initRouletteEmojis() {
        emojis.add(binding.emojiAppCompatEditText4)
        emojis.add(binding.emojiAppCompatEditText)
        emojis.add(binding.emojiAppCompatEditText6)
        emojis.add(binding.emojiAppCompatEditText8)
        emojis.add(binding.emojiAppCompatEditText9)
        emojis.add(binding.emojiAppCompatEditText10)
        emojis.add(binding.emojiAppCompatEditText7)
        emojis.add(binding.appCompatImageView3)
        emojis.add(binding.emojiAppCompatEditText3)
        emojis.add(binding.emojiAppCompatEditText11)
        emojis.add(binding.emojiAppCompatEditText2)
        emojis.add(binding.emojiAppCompatEditText5)

        for (emoji in emojis) {
            emoji.setText(Emoji.getRandomEmoji())
        }

        emojis[3].setText("\uD83C\uDF9F️")
        emojis[9].setText("")
        emojis[4].setText("\uD83C\uDF81")
    }

    private fun initRouletteAnimation() {
        val toDegrees = getRandomDegree()
        rotateAnimator = RotateAnimation(
            0f,
            toDegrees, 1, 0.5f, 1, 0.5f
        )

        rotateAnimator.duration = 5000
        rotateAnimator.fillAfter = true
        rotateAnimator.interpolator = DecelerateInterpolator()
        rotateAnimator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            @SuppressLint("TimberArgCount")
            override fun onAnimationEnd(animation: Animation?) {
                val emoji = ((toDegrees / 30).toInt() % 12)
                givePrize(emojis[emoji].text.toString())
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        binding.rouletteConstraint.animation = rotateAnimator
    }

    private fun givePrize(prize: String) {
        when(prize){
            "\uD83C\uDF9F️" -> showTicketDialog()
            "\uD83C\uDF81" -> showRandomPrizeDialog()
            else -> showSimpleEmojiDialog()
        }
    }

    private fun showSimpleEmojiDialog() {

    }

    private fun showRandomPrizeDialog() {

    }

    private fun showTicketDialog() {

    }

    private fun getRandomDegree(): Float {
        var startDegree = 720f
        for(i in 30..1200 step 30){
            startDegree += i
            if(Random.nextBoolean()){
                break
            }
        }
        return startDegree
    }

    private fun initWatchAdButton() {
        binding.chestForAddBtn.setOnClickListener {
            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
            }
        }
    }

    private fun initRewardedAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(requireContext())
        mRewardedVideoAd.rewardedVideoAdListener = object : MyRewardedVideoListener() {
            override fun onRewardedVideoAdOpened() {
                super.onRewardedVideoAdOpened()
                mRewardedVideoAd.loadAd(
                    REWARDED_VIDEO_ID,
                    AdRequest.Builder().build()
                )
            }

            override fun onRewarded(p0: RewardItem?) {
                super.onRewarded(p0)
                viewModel.updateUserBoxes(values.boxes + 1)
                binding.boxesCount.text =
                    (binding.boxesCount.text.toString().toInt() + 1).toString()
            }
        }

        mRewardedVideoAd.loadAd(
            REWARDED_VIDEO_ID,
            AdRequest.Builder().build()
        )
    }

    private fun initBuyChestButton() {
        binding.rollWheelBtn.setOnClickListener {
            initRouletteAnimation()
            val emos = binding.emosCount.text.toString().toInt()
            if (emos > 200) {
                viewModel.updateUserEmos(emos - 200)
                binding.emosCount.text = (emos - 200).toString()
                startRoulette()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.not_enough_emos), Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.rollTicket.setOnClickListener {
            initRouletteAnimation()
            val tickets = binding.boxesCount.text.toString().toInt()
            if (tickets > 0) {
                viewModel.updateUserEmos(tickets - 1)
                binding.boxesCount.text = (tickets - 1).toString()
                startRoulette()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.not_enough_tickets), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startRoulette() {
        binding.rouletteConstraint.startAnimation(rotateAnimator)
    }

    private fun getValuesFromBundle() {
        values = LootBoxesFragmentArgs.fromBundle(requireArguments()).Values
        binding.values = values
    }

    private fun initWinningEmojiButtons(emojiShopModel: EmojiShopModel) {
//        binding.sellWinningEmoji.text = EmojiCost.emojiSellCost(emojiShopModel)
//        val cost = EmojiCost.getEmojiSellCost(binding.sellWinningEmoji)
//        binding.takeWinningEmoji.setOnClickListener {
//
//            emojiWinButtons.visibility = View.INVISIBLE
//
//
//            viewModel.addEmoji(emojiShopModel,0,getUserValues())
//
//            updateUserEmojisCount()
//
//            (activity as MainActivity).mediaPlayerPool.play(MusicType.WIN)
//        }

//        binding.sellWinningEmoji.setOnClickListener {
//
//            emojiWinButtons.visibility = View.INVISIBLE
//
//            val emos = binding.emosCount.text.toString().toInt() + cost
//
//            viewModel.updateUserEmos(emos)
//
//            binding.emosCount.text = emos.toString()
//
//            (activity as MainActivity).mediaPlayerPool.play(MusicType.LOSE)
//        }
    }

    private fun initOpenBoxButton() {

    }

    private fun updateUserEmojisCount() {
        val count = binding.emojisCount.text.toString().toInt() + 1
        binding.emojisCount.text = count.toString()
    }

    private fun updateUserBoxes() {
        val boxesCount = binding.boxesCount.text.toString().toInt() - 1
        viewModel.updateUserBoxes(boxesCount)
        binding.boxesCount.text = (boxesCount).toString()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onPause() {
        super.onPause()
        mRewardedVideoAd.pause(requireContext())
    }

    override fun onResume() {
        super.onResume()
        mRewardedVideoAd.resume(requireContext())
    }

    private fun initAdapter() {
        adapter = LootBoxRecyclerViewAdapter(LootBoxRecyclerViewAdapter.OnShopItemClickListener {

        })
    }

    private fun initShopViewModel() {
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        adapter.submitList(it.data)
                        initOpenBoxButton()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mRewardedVideoAd.destroy(requireContext())
    }

    private fun getUserValues(): AccountValuesModel {
        return AccountValuesModel(
            emos = binding.emosCount.text.toString().toInt(),
            boxes = binding.boxesCount.text.toString().toInt(),
            emojis = binding.emojisCount.text.toString().toInt()
        )
    }

}
