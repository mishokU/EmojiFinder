package com.example.emojifinder.ui.localization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.emoji.widget.EmojiAppCompatButton
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentLocalizeAppBinding
import com.example.emojifinder.domain.locale.LocaleHelper
import dagger.android.support.DaggerFragment


class LocalizeAppFragment : DaggerFragment() {


    private lateinit var binding : FragmentLocalizeAppBinding
    private var mapOfCountries : HashMap<EmojiAppCompatButton, Boolean> = hashMapOf()

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
            LocaleHelper.setLocale(requireContext(), getSelectedLanguage())
            //requireActivity().recreate()
        }
    }

    private fun getSelectedLanguage(): String {
        for(country in mapOfCountries){
            if(country.value){
                when(country.key) {
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
        mapOfCountries[binding.americanFrag] = true
        mapOfCountries[binding.russianFlag] = false
        mapOfCountries[binding.dutchFlag] = false
        mapOfCountries[binding.indieFlag] = false

        handleLanguage(binding.americanFrag)
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
        for(button in mapOfCountries){
            if(button.key == country){
                button.key.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.emoji_style_active
                )
                mapOfCountries[button.key] = true
            }
            else {
                button.key.background = ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.emoji_style
                )
                mapOfCountries[button.key] = false
            }
        }
    }
}
