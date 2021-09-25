package com.mishok.emojifinder.ui.auth.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentSignInBinding
import com.mishok.emojifinder.domain.locale.LocaleHelper
import com.mishok.emojifinder.domain.viewModels.SharedViewModel
import com.mishok.emojifinder.domain.viewModels.SignInViewModel
import com.mishok.emojifinder.ui.localization.AppLanguages.DUTCH
import com.mishok.emojifinder.ui.localization.AppLanguages.ENGLISH
import com.mishok.emojifinder.ui.localization.AppLanguages.HINDI
import com.mishok.emojifinder.ui.localization.AppLanguages.RUSSIAN
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject


class SignInFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : SignInViewModel

    private val viewModelShared : SharedViewModel by activityViewModels()

    private lateinit var binding : FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater)
        handleButtons()
        initObserveLanguage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLanguageIcon()
    }

    private fun initLanguageIcon() {
        when(LocaleHelper.getLanguage(requireContext())){
            ENGLISH -> language_iv.setText(R.string.emoji_american_flag)
            RUSSIAN -> language_iv.setText(R.string.emoji_russian_flag)
            DUTCH -> language_iv.setText(R.string.emoji_german_flag)
            HINDI -> language_iv.setText(R.string.emoji_hindi_flag)
        }
    }

    private fun initObserveLanguage() {
        viewModelShared.languageFlag.observe(viewLifecycleOwner, { language ->
            when(language){
                ENGLISH -> language_iv.setText(R.string.emoji_american_flag)
                RUSSIAN -> language_iv.setText(R.string.emoji_russian_flag)
                DUTCH -> language_iv.setText(R.string.emoji_german_flag)
                HINDI -> language_iv.setText(R.string.emoji_hindi_flag)
            }
        })
    }

    private fun handleButtons() {
        binding.loginBtn.setOnClickListener {
            this.findNavController().navigate(R.id.logInFragment)
        }
        binding.createAccountBtn.setOnClickListener {
            this.findNavController().navigate(R.id.registrationFragment)
        }
        binding.languageIv.setOnClickListener {
            this.findNavController().navigate(R.id.localizeAppFragment)
        }
        binding.helpIv.setOnClickListener {
            this.findNavController().navigate(R.id.helpFragment)
        }
    }
}
