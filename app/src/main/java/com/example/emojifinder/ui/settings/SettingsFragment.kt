package com.example.emojifinder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentSettingsBinding
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.domain.viewModels.LogInViewModel
import com.example.emojifinder.ui.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : LogInViewModel

    lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsPrefs: SettingsPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)

        DeleteAccountDialog.create(this)
        LogOutDialog.create(this)

        initDialogButtons()
        setLogOutButton()

        initSwitchers()

        return binding.root
    }

    private fun initDialogButtons() {
        LogOutDialog.getLogOutButton().setOnClickListener {
            viewModel.logOut()

            (activity as MainActivity).navigateFirstTabWithClearStack()

            this.findNavController().navigate(R.id.signInFragment)
            LogOutDialog.dialogView.dismiss()
        }

        DeleteAccountDialog.getDeleteAccountBtn().setOnClickListener {
            viewModel.deleteAccount()

            (activity as MainActivity).navigateFirstTabWithClearStack()
            this.findNavController().navigate(R.id.signInFragment)

            DeleteAccountDialog.dialogView.dismiss()
        }
    }

    private fun initSwitchers() {
        binding.musicSwitcher.isChecked = settingsPrefs.isPlayMusic()
        binding.musicSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                settingsPrefs.changeMusic(isChecked)
                (activity as MainActivity).mediaPlayerPool.createPlayers()
                (activity as MainActivity).mediaPlayerPool.playBackground()
            } else {
                (activity as MainActivity).mediaPlayerPool.pauseBackground()
                settingsPrefs.changeMusic(isChecked)
            }
        }
    }

    private fun setLogOutButton() {
        viewModel = injectViewModel(viewModelFactory)
        binding.exitBtn.setOnClickListener {
            LogOutDialog.open()
        }
        binding.deleteAccountBtn.setOnClickListener {
            DeleteAccountDialog.open()
        }
        binding.suggestLevelBtn.setOnClickListener {
            this.findNavController().navigate(R.id.yourLevelsFragment)
        }

        binding.helpImg.text = "ℹ️"
        binding.helpBtn.setOnClickListener {
            this.findNavController().navigate(R.id.helpFragment)
        }

        binding.privatePolicyImg.text = "\uD83D\uDD75️"
        binding.privatePolicyBtn.setOnClickListener {

        }
    }
}
