package com.example.emojifinder.ui.boxes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.emoji.widget.EmojiAppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.databinding.FragmentLootBoxesBinding
import com.example.emojifinder.domain.adds.MyRewardedVideoListener
import com.example.emojifinder.domain.adds.REWARDED_VIDEO_ID
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.random.Random


class LootBoxesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AccountViewModel

    lateinit var binding: FragmentLootBoxesBinding
    lateinit var values: AccountValuesModel

    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private var emojis: MutableList<EmojiAppCompatEditText> = mutableListOf()
    private var emojisModel: MutableList<EmojiShopModel> = mutableListOf()
    private lateinit var rotateAnimator: RotateAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLootBoxesBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)

        binding.lifecycleOwner = this

        getValuesFromBundle()

        initRouletteEmojis()

        initValues()
        initBuyChestButton()
        initWatchAdButton()

        initRewardedAd()


        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initValues() {
        binding.emojiBoxEt.setText(resources.getString(R.string.emoji_ticket))
        binding.emosEt.setText(resources.getString(R.string.emoji_emos))
        binding.emojiEt.setText(resources.getString(R.string.simple_emoji))
        binding.rollWheelBtn.text = resources.getString(R.string.emoji_emos) + " 200"
        binding.rollTicket.text = resources.getString(R.string.emoji_ticket) + " 1"
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
            val emojiModel = (activity as MainActivity).randomEmojis.random()
            emojisModel.add(emojiModel)
            emoji.setText(emojiModel.text)
        }

        emojisModel[3].text = resources.getString(R.string.emoji_ticket)
        emojisModel[4].text = resources.getString(R.string.emoji_present)
        emojisModel[9].text = ""

        emojis[3].setText(resources.getString(R.string.emoji_ticket))
        emojis[9].setText("")
        emojis[4].setText(resources.getString(R.string.emoji_present))
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
                givePrize(emojisModel[emoji])
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        binding.rouletteConstraint.animation = rotateAnimator
    }

    private fun givePrize(prize: EmojiShopModel) {
        when (prize.text) {
            resources.getString(R.string.emoji_ticket) -> showTicketDialog()
            resources.getString(R.string.emoji_present) -> showRandomPrizeDialog()
            "" -> showNothingDialog()
            else -> showSimpleEmojiDialog(prize)
        }
    }

    private fun showNothingDialog() {
        NothingEmojiDialog.create(this)
        NothingEmojiDialog.show()
    }

    private fun showSimpleEmojiDialog(prize: EmojiShopModel) {
        WinEmojiDialog.create(this)
        WinEmojiDialog.show(prize)
        viewModel.addGeneratedEmoji(prize)
        updateUserEmojisCount()
        viewModel.updateUserEmojis(getUserValues().emojis + 1)
    }

    private fun showRandomPrizeDialog() {
        RandomPrizeDialog.create(this)
        RandomPrizeDialog.show()
        RandomPrizeDialog.emoji.observe(viewLifecycleOwner, Observer { emoji ->
            viewModel.addGeneratedEmoji(emoji)
            updateUserEmojisCount()
            viewModel.updateUserEmojis(getUserValues().emojis + 1)
        })
    }

    private fun showTicketDialog() {
        TicketEmojiDialog.create(this)
        TicketEmojiDialog.show()
        updateUserTickets()
    }

    private fun getRandomDegree(): Float {
        var startDegree = 720f
        for (i in 30..1200 step 30) {
            startDegree += i
            if (Random.nextBoolean()) {
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
                binding.rouletteConstraint.startAnimation(rotateAnimator)
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
                viewModel.updateUserBoxes(tickets - 1)
                binding.boxesCount.text = (tickets - 1).toString()
                binding.rouletteConstraint.startAnimation(rotateAnimator)
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.not_enough_tickets), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getValuesFromBundle() {
        values = LootBoxesFragmentArgs.fromBundle(requireArguments()).Values
        binding.values = values
    }

    private fun updateUserEmojisCount() {
        val count = binding.emojisCount.text.toString().toInt() + 1
        binding.emojisCount.text = count.toString()
    }

    private fun updateUserTickets() {
        val ticketsCount = binding.boxesCount.text.toString().toInt() - 1
        viewModel.updateUserBoxes(ticketsCount)
        binding.boxesCount.text = (ticketsCount).toString()
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
