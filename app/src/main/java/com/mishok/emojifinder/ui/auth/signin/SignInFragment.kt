package com.mishok.emojifinder.ui.auth.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentSignInBinding
import com.mishok.emojifinder.domain.viewModels.SignInViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SignInFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : SignInViewModel

    private lateinit var binding : FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignInBinding.inflate(inflater)
        // Inflate the layout for this fragment
        handleButtons()
        return binding.root
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
