package com.example.emojifinder.ui.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentHelpBinding
import dagger.android.support.DaggerFragment

class HelpFragment : DaggerFragment() {

    private lateinit var binding : FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpBinding.inflate(inflater)
        // Inflate the layout for this fragment

        initToolbar()
        initFab()

        return binding.root
    }

    private fun initFab() {
        binding.writeQuestion.setOnClickListener {
            this.findNavController().navigate(R.id.writeQuestionFragment)
        }
    }

    private fun initToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.helpToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.helpToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }
}
