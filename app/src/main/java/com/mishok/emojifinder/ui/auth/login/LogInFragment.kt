package com.mishok.emojifinder.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentLogInBinding
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.viewModels.LogInViewModel
import com.mishok.emojifinder.ui.game.campaign.gameAlerts.ErrorDialog
import com.mishok.emojifinder.ui.utils.CustomTextWatcher
import com.mishok.emojifinder.ui.utils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LogInFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: LogInViewModel

    lateinit var binding: FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater)

        ErrorDialog.create(this)

        handleRegistrationStatus()
        handleLogInButton()
        handleForgetPassword()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.loginLoginBtn.isEnabled = (
                binding.emailLogIn.text.toString().isNotEmpty() &&
                        binding.passwordLogIn.text.toString().isNotEmpty()
                )
    }

    private fun handleForgetPassword() {
        binding.forgotPasswordBtn.setOnClickListener {
            this.findNavController().navigate(R.id.forgetPasswordFragment)
        }
        binding.registrationLogin.setOnClickListener {
            this.findNavController().navigate(R.id.registrationFragment)
        }
    }

    private fun handleLogInButton() {
        bindProgressButton(binding.loginLoginBtn)
        binding.emailLogIn.addTextChangedListener(loginTextWatcher)
        binding.passwordLogIn.addTextChangedListener(loginTextWatcher)
        binding.loginLoginBtn.setOnClickListener {
            viewModel.login(binding.emailLogIn, binding.passwordLogIn)
            ((activity as DaggerAppCompatActivity)).hideKeyboard()
        }
    }

    private fun handleRegistrationStatus() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        this.findNavController().popBackStack(R.id.signInFragment, true);
                        this.findNavController().navigate(R.id.mainMenuFragment)
                    }
                    is Result.Loading -> {
                        binding.loginLoginBtn.showProgress {
                            progressColor =
                                ContextCompat.getColor(requireContext(), R.color.background_color)
                            buttonText = ""
                        }
                    }
                    is Result.Error -> {
                        binding.loginLoginBtn.hideProgress(resources.getString(R.string.login))
                        ErrorDialog.setErrorText(it.exception.message)
                    }
                }
            }
        })
    }

    private val loginTextWatcher = object : CustomTextWatcher() {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.loginLoginBtn.isEnabled = (
                    binding.emailLogIn.text.toString().isNotEmpty() &&
                            binding.passwordLogIn.text.toString().isNotEmpty())
        }
    }

}
