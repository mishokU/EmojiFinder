package com.example.emojifinder.ui.account

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.databinding.FragmentMainAccountInfoBinding
import com.example.emojifinder.domain.viewModels.AccountViewModel
import com.example.emojifinder.domain.viewModels.SharedViewModel
import com.example.emojifinder.ui.main.MainActivity
import com.example.emojifinder.ui.utils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import java.nio.channels.Selector
import javax.inject.Inject


class MainAccountInfoFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : AccountViewModel

    private val viewModelShared : SharedViewModel by activityViewModels()

    lateinit var binding : FragmentMainAccountInfoBinding
    lateinit var profile : MainAccountModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainAccountInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = injectViewModel(viewModelFactory)


        changeSoftInput()
        addTextWatchers()

        getUserMainData()

        setBackButton()
        setSaveButton()
        setAvatarButton()
        setCheckEmailAndPasswordButtons()

        return binding.root
    }

    private fun changeSoftInput() {
        (activity as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun setAvatarButton() {
        binding.goToUserAvatar.setOnClickListener {
            this.findNavController().navigate(MainAccountInfoFragmentDirections.actionMainAccountInfoFragmentToAccountAvatarFragment(
                profile
            ))
        }
    }

    private fun setCheckEmailAndPasswordButtons() {
        binding.checkPasswordBtn.setOnClickListener {
            if(binding.currentPassword.text.toString() == binding.passwordMainInfo.text.toString()){
                binding.emailMainInfoTil.visibility = View.VISIBLE
                binding.passwordMainInfoTil.visibility = View.VISIBLE
                binding.checkPlace.visibility = View.GONE

                viewModel.oldEmail = binding.emailRegistration.text.toString()
                viewModel.oldPassword = binding.passwordMainInfo.text.toString()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun addTextWatchers() {
        binding.loginRegistration.addTextChangedListener(registrationTextWatcher)
        binding.emailRegistration.addTextChangedListener(registrationTextWatcher)
        binding.passwordMainInfo.addTextChangedListener(registrationTextWatcher)
    }

    private fun setSaveButton() {
        binding.updateLoginBtn.setOnClickListener {
            viewModelShared.updateLoginView(binding.loginRegistration.text.toString())
            viewModel.updateLogin(binding.loginRegistration)
        }

        binding.saveEmailPasswordBtn.setOnClickListener {
            viewModel.updateUserEmailAndPassword(
                binding.emailRegistration,
                binding.passwordMainInfo
            )
            ((activity as DaggerAppCompatActivity)).hideKeyboard()
        }
    }

    private fun getUserMainData() {
        profile = MainAccountInfoFragmentArgs.fromBundle(requireArguments()).MainInfo
        binding.profile = profile
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarMainInfo)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarMainInfo.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private val registrationTextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.saveEmailPasswordBtn.isEnabled = (
                    binding.emailRegistration.text.toString().isNotEmpty() &&
                    binding.passwordMainInfo.text.toString().isNotEmpty())

            binding.updateLoginBtn.isEnabled = binding.loginRegistration.text.toString().isNotEmpty()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

}
