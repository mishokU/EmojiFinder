package com.example.emojifinder.ui.auth.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentRegistrationBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.RegistrationViewModel
import com.example.emojifinder.ui.game.campaign.gameAlerts.ErrorDialog
import com.example.emojifinder.ui.utils.hideKeyboard
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RegistrationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : RegistrationViewModel

    private lateinit var binding : FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)

        ErrorDialog.create(this)

        initToolbar()

        addTextWatchers()
        handleRegistrationButton()
        handleRegistrationStatus()

        return binding.root
    }

    private fun initToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.registrationToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.registrationToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun addTextWatchers() {
        binding.loginRegistration.addTextChangedListener(registrationTextWatcher)
        binding.emailRegistration.addTextChangedListener(registrationTextWatcher)
        binding.passwordRegistration.addTextChangedListener(registrationTextWatcher)
        binding.repeatPasswordRegistration.addTextChangedListener(registrationTextWatcher)
    }

    private fun handleRegistrationStatus() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.registrationResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.registrationBtn.showProgress {
                            progressColor = ContextCompat.getColor(requireContext(),R.color.background_color)
                            buttonText = ""
                        }
                    }
                    is Result.Success -> {
                        this.findNavController().popBackStack(R.id.signInFragment, true);
                        this.findNavController().navigate(R.id.mainMenuFragment)
                    }
                    is Result.Error -> {
                        ErrorDialog.setErrorText(it.exception.message)
                        binding.registrationBtn.hideProgress(R.string.registration)
                    }
                }
            }
        })
    }

    private fun handleRegistrationButton() {
        bindProgressButton(binding.registrationBtn)
        binding.registrationBtn.setOnClickListener {
                viewModel.registration(
                    binding.loginRegistration,
                    binding.emailRegistration,
                    binding.passwordRegistration,
                    binding.repeatPasswordRegistration
                )
            ((activity as DaggerAppCompatActivity)).hideKeyboard()
        }
    }

    private val registrationTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.registrationBtn.isEnabled = (
                    binding.loginRegistration.text.toString().isNotEmpty() &&
                    binding.emailRegistration.text.toString().isNotEmpty() &&
                    binding.passwordRegistration.text.toString().isNotEmpty() &&
                    binding.repeatPasswordRegistration.text.toString().isNotEmpty())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}
