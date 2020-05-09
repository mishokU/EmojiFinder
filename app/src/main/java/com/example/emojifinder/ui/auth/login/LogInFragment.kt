package com.example.emojifinder.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentLogInBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.LogInViewModel
import com.example.emojifinder.ui.utils.ErrorDialog
import com.example.emojifinder.ui.utils.hideKeyboard
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LogInFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : LogInViewModel

    lateinit var binding : FragmentLogInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater)
        // Inflate the layout for this fragment

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
                when(it){
                    is Result.Success -> {
                        this.findNavController().popBackStack(R.id.signInFragment, true);
                        this.findNavController().navigate(R.id.mainMenuFragment)
                    }
                    is Result.Loading -> {
                        binding.loginLoginBtn.showProgress {
                            progressColor = ContextCompat.getColor(requireContext(),R.color.background_color)
                            buttonText = ""
                        }
                    }
                    is Result.Error -> {
                        binding.loginLoginBtn.hideProgress(resources.getString(R.string.login))
                        ErrorDialog.show(this, it.exception.message)
                    }
                }
            }
        })
    }

    private val loginTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.loginLoginBtn.isEnabled = (
                    binding.emailLogIn.text.toString().isNotEmpty() &&
                    binding.passwordLogIn.text.toString().isNotEmpty())
        }

        override fun afterTextChanged(s: Editable?) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }

}
