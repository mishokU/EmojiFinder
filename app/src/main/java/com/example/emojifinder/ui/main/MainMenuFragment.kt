package com.example.emojifinder.ui.main

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

        binding.shopMainBtn.setOnClickListener {
            this.findNavController().navigate(R.id.accountAvatarFragment)
        }

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
        binding.emojiTextView2.text = EmojiCompat.get().process("\uD83E\uDD0F\uD83C\uDFFF")
        binding.emojiTextView4.text = Emoji.getEmojiByUnicode(unicode = 0x1F64F)
        binding.emojiTextView6.text = Emoji.getEmojiByUnicode(unicode = 0x1F44D)
        binding.emojiTextView3.text = Emoji.getEmojiByUnicode(unicode = 0x1F764)
    }
}
