package com.example.emojifinder.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentMainMenuBinding
import com.example.emojifinder.domain.prefs.DailyWinningsPrefs
import com.example.emojifinder.shared.utils.Emoji
import com.example.emojifinder.ui.daily.DailyUI
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MainMenuFragment : DaggerFragment() {

    lateinit var binding : FragmentMainMenuBinding

    @Inject
    lateinit var daily : DailyWinningsPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMenuBinding.inflate(inflater)

        initEmojies()
        handleButtons()
        setPopularEmojis()
        addListenerToAdView()
        dailyWinnings()

        return binding.root
    }

    private fun dailyWinnings() {
        if(daily.isNextDay()){
            this.findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToDailyWinningsFragment(
                DailyUI(daily.getDay())
            ))
        }
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

    private fun handleButtons() {

        binding.startArcadeGameBtn.setOnClickListener {
            this.findNavController().navigate(R.id.arcadeGameFragment)
        }

        binding.startCategoryGameBtn.setOnClickListener {
            this.findNavController().navigate(R.id.categotyGameFragment)
        }
        binding.profileMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.accountFragment)
        }
        binding.settingsMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.settingsFragment)
        }

        binding.shopMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.accountAvatarFragment)
        }

        binding.shopBtn.setOnClickListener {
            this.findNavController().navigate(R.id.shopFragment)
        }
    }

    private fun setPopularEmojis() {
        binding.emojiTextView2.setOnClickListener {
            binding.emojiTextView2.text = Emoji.getRandomEmoji()
        }

        binding.emojiTextView3.setOnClickListener {
            binding.emojiTextView3.text = Emoji.getRandomEmoji()
        }

        binding.emojiTextView4.setOnClickListener {
            binding.emojiTextView4.text = Emoji.getRandomEmoji()
        }

        binding.emojiTextView6.setOnClickListener {
            binding.emojiTextView6.text = Emoji.getRandomEmoji()
        }
    }

    private fun initEmojies() {
        binding.emojiTextView2.text = Emoji.getEmojiByString("\uD83D\uDE80")
        binding.emojiTextView4.text = Emoji.getEmojiByString("\uD83D\uDC69")
        binding.emojiTextView3.text = Emoji.getEmojiByString("\uD83C\uDF6D")
        binding.emojiTextView6.text = Emoji.getEmojiByString("\uD83C\uDF0E")
    }
}
