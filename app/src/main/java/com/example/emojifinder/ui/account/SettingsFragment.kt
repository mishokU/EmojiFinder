package com.example.emojifinder.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentSettingsBinding
import com.example.emojifinder.domain.viewModels.LogInViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : LogInViewModel

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        // Inflate the layout for this fragment

        setLogOutButton()
        setBackButton()

        return binding.root
    }

    private fun setLogOutButton() {
        viewModel = injectViewModel(viewModelFactory)
        binding.exitBtn.setOnClickListener {
            viewModel.logOut()
            this.findNavController().popBackStack(R.id.settingsFragment, true)
            this.findNavController().navigate(R.id.signInFragment)
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
