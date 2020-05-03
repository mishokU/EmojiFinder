package com.example.emojifinder.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
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
        // Inflate the layout for this fragment
        initEmojies()
        handleButtons()

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
    }

    private fun initEmojies() {
        binding.emojiTextView2.text = Emoji.getEmojiByUnicode(unicode = 0x1F973)
        binding.emojiTextView4.text = Emoji.getEmojiByUnicode(unicode = 0x1F64F)
        binding.emojiTextView6.text = Emoji.getEmojiByUnicode(unicode = 0x1F44D)
        binding.emojiTextView3.text = Emoji.getEmojiByUnicode(unicode = 0x2764)
    }
}