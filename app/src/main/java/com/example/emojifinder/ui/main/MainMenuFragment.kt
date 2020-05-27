package com.example.emojifinder.ui.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.emoji.text.EmojiCompat
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentMainMenuBinding
import com.example.emojifinder.shared.utils.Emoji
import dagger.android.support.DaggerFragment


class MainMenuFragment : DaggerFragment() {

    lateinit var binding : FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMenuBinding.inflate(inflater)

        initEmojies()
        handleButtons()
        setPopularEmojis()

        return binding.root
    }

    private fun handleButtons() {

        binding.startArcadeGameBtn.setOnClickListener {
            this.findNavController().navigate(R.id.gameFragment)
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

        binding.helpMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.helpFragment)
        }

        binding.exitBtn.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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
