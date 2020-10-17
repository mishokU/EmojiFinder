package com.mishok.emojifinder.ui.localization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.emoji.widget.EmojiAppCompatButton
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.FragmentLocalizeAppBinding
import com.mishok.emojifinder.domain.locale.LocaleHelper
import dagger.android.support.DaggerFragment


class LocalizeAppFragment : DaggerFragment() {

    private lateinit var binding: FragmentLocalizeAppBinding
    private var mapOfCountries: HashMap<EmojiAppCompatButton, Boolean> = hashMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocalizeAppBinding.inflate(inflater)

        initArrayOfCountries()
        initCountries()
        initSaveLanguageButton()

        return binding.root
    }

    private fun initSaveLanguageButton() {
        binding.saveLanguageBtn.setOnClickListener {
            LocaleHelper.setLocale(requireActivity().baseContext, getSelectedLanguage())
            binding.saveLanguageBtn.text = resources.getString(R.string.save_info)
            binding.ratingTitle.text = resources.getString(R.string.rating)
        }
    }

    private fun getSelectedLanguage(): String {
        for (country in mapOfCountries) {
            if (country.value) {
                when (country.key) {
                    binding.americanFrag -> return "en"
                    binding.dutchFlag -> return "de"
                    binding.indieFlag -> return "hi"
                    binding.russianFlag -> return "ru"
                }
            }
        }
        return "en"
    }

    private fun initArrayOfCountries() {
        mapOfCountries[binding.russianFlag] = true
        mapOfCountries[binding.americanFrag] = false
        mapOfCountries[binding.dutchFlag] = false
        mapOfCountries[binding.indieFlag] = false
        when(LocaleHelper.getLanguage(requireActivity().baseContext)){
            "ru" -> handleLanguage(binding.russianFlag)
            "en" -> handleLanguage(binding.americanFrag)
            "hi" -> handleLanguage(binding.indieFlag)
            "de" -> handleLanguage(binding.dutchFlag)
        }
    }

    private fun initCountries() {
        binding.americanFrag.setOnClickListener {
            handleLanguage(binding.americanFrag)
        }
        binding.russianFlag.setOnClickListener {
            handleLanguage(binding.russianFlag)
        }
        binding.dutchFlag.setOnClickListener {
            handleLanguage(binding.dutchFlag)
        }
        binding.indieFlag.setOnClickListener {
            handleLanguage(binding.indieFlag)
        }
    }

    private fun handleLanguage(country: EmojiAppCompatButton) {
        for (button in mapOfCountries) {
            if (button.key == country) {
                button.key.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.emoji_style_active
                )
                mapOfCountries[button.key] = true
            } else {
                button.key.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.emoji_style
                )
                mapOfCountries[button.key] = false
            }
        }
    }
}
