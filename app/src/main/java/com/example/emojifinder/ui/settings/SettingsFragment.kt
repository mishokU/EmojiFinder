package com.example.emojifinder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
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
        setBackButton()

        initSwitchers()

        return binding.root
    }

    private fun initDialogButtons() {
        LogOutDialog.getLogOutButton().setOnClickListener {
            viewModel.logOut()
            this.findNavController().popBackStack(R.id.settingsFragment, true)
            this.findNavController().navigate(R.id.signInFragment)
            LogOutDialog.dialogView.dismiss()
        }

        DeleteAccountDialog.getDeleteAccountBtn().setOnClickListener {
            viewModel.deleteAccount()

            this.findNavController().popBackStack(R.id.settingsFragment, true)
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

//    fun navigateFirstTabWithClearStack() {
//        val navController = findNavController(R.id.nav_host_main)
//        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
//        val inflater = navHostFragment.navController.navInflater
//        val graph = inflater.inflate(R.navigation.nav_graph_main)
//        graph.startDestination = R.id.nav_graph_tab1
//        navController.graph = graph
//    }

    private fun setLogOutButton() {
        viewModel = injectViewModel(viewModelFactory)
        binding.exitBtn.setOnClickListener {
            LogOutDialog.open()
        }
        binding.deleteAccountBtn.setOnClickListener {
            DeleteAccountDialog.open()
        }
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarSettings)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarSettings.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
