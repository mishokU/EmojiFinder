package com.example.emojifinder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.data.db.local.room.dao.LevelMainInfoDao
import com.example.emojifinder.databinding.FragmentSettingsBinding
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.domain.viewModels.LogInViewModel
import com.example.emojifinder.ui.main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: LogInViewModel

    lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var settingsPrefs: SettingsPrefs

    @Inject
    lateinit var levelsDao : LevelMainInfoDao
    var destination : Int = R.id.levelConstructorFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)

        viewModel = injectViewModel(viewModelFactory)

        DeleteAccountDialog.create(this)
        LogOutDialog.create(this)

        initLaunchConstructor()
        initDialogButtons()
        setLogOutButton()

        initSwitchers()

        return binding.root
    }

    private fun initLaunchConstructor() {
        levelsDao.getLevels().observe(viewLifecycleOwner, Observer {
            destination = if(it.isNullOrEmpty()){
                R.id.levelConstructorFragment
            } else {
                R.id.yourLevelsFragment
            }
        })
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
            if (isChecked) {
                settingsPrefs.changeMusic(isChecked)
                (activity as MainActivity).mediaPlayerPool.createPlayers()
                (activity as MainActivity).mediaPlayerPool.playBackground()
            } else {
                (activity as MainActivity).mediaPlayerPool.pauseBackground()
                settingsPrefs.changeMusic(isChecked)
            }
        }

        binding.notificationsSwitcher.isChecked = settingsPrefs.isNotificationsAvailable()
        binding.notificationsSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsPrefs.changeNotifications(isChecked)
            }
        }

    }

    private fun setLogOutButton() {
        binding.exitBtn.setOnClickListener {
            LogOutDialog.open()
        }
        binding.deleteAccountBtn.setOnClickListener {
            DeleteAccountDialog.open()
        }
        binding.suggestLevelBtn.setOnClickListener {
            this.findNavController().navigate(destination)
        }

        binding.helpImg.text = resources.getString(R.string.emoji_help)
        binding.helpBtn.setOnClickListener {
            this.findNavController().navigate(R.id.helpFragment)
        }

        binding.privatePolicyImg.text = resources.getString(R.string.emoji_private_policy)
        binding.privatePolicyBtn.setOnClickListener {

        }
    }
}
