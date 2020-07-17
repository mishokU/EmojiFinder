package com.example.emojifinder.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentForgetPasswordBinding
import com.example.emojifinder.domain.auth.CheckOnValid
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.LogInViewModel
import com.example.emojifinder.ui.game.campaign.gameAlerts.ErrorDialog
import com.example.emojifinder.ui.utils.CustomTextWatcher
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ForgetPasswordFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: LogInViewModel

    lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater)

        ErrorDialog.create(this)

        handleEmailStatus()
        handleButton()

        return binding.root
    }

    private fun handleButton() {
        binding.emailForget.addTextChangedListener(emailTextWatcher)
        binding.forgetPasswordBtn.setOnClickListener {
            if (CheckOnValid.isEmailValid(binding.emailForget)) {
                viewModel.forgetPassword(binding.emailForget)
            }
        }
    }

    private fun handleEmailStatus() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.restorePasswordResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        this.findNavController().popBackStack(R.id.forgetPasswordFragment, true)
                        this.findNavController().navigate(R.id.logInFragment)
                    }
                    is Result.Loading -> {
                        binding.forgetPasswordBtn.showProgress {
                            progressColor =
                                ContextCompat.getColor(requireContext(), R.color.background_color)
                            buttonText = ""
                        }
                    }
                    is Result.Error -> {
                        binding.forgetPasswordBtn.hideProgress(resources.getString(R.string.forgot_your_password))
                        ErrorDialog.setErrorText(it.exception.message)
                    }
                }
            }
        })
    }

    private val emailTextWatcher = object : CustomTextWatcher() {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.forgetPasswordBtn.isEnabled = (binding.emailForget.text.toString().isNotEmpty())
        }
    }

}
