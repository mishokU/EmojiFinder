package com.mishok.emojifinder.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mishok.emojifinder.R
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.databinding.FragmentMainAccountInfoBinding
import com.mishok.emojifinder.domain.viewModels.AccountViewModel
import com.mishok.emojifinder.domain.viewModels.SharedViewModel
import com.mishok.emojifinder.ui.main.MainActivity
import com.mishok.emojifinder.ui.utils.CustomTextWatcher
import com.mishok.emojifinder.ui.utils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
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
        binding.profileEmojiAvatarMain.setOnClickListener {
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
            Toast.makeText(requireContext(),resources.getString(R.string.login_updated), Toast.LENGTH_SHORT).show()
            ((activity as DaggerAppCompatActivity)).hideKeyboard()
        }

        binding.saveEmailPasswordBtn.setOnClickListener {
            viewModel.updateUserEmailAndPassword(
                binding.emailRegistration,
                binding.passwordMainInfo
            )
            Toast.makeText(requireContext(),resources.getString(R.string.email_and_password_updated), Toast.LENGTH_SHORT).show()
            ((activity as DaggerAppCompatActivity)).hideKeyboard()
        }
    }

    private fun getUserMainData() {
        profile = MainAccountInfoFragmentArgs.fromBundle(requireArguments()).MainInfo
        binding.profile = profile
    }

    private val registrationTextWatcher = object : CustomTextWatcher() {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            binding.saveEmailPasswordBtn.isEnabled = (
                    binding.emailRegistration.text.toString().isNotEmpty() &&
                    binding.passwordMainInfo.text.toString().isNotEmpty())

            binding.updateLoginBtn.isEnabled = binding.loginRegistration.text.toString().isNotEmpty()
        }
    }

}
