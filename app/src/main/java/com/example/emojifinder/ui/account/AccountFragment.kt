package com.example.emojifinder.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentAccountBinding
import dagger.android.support.DaggerFragment


class AccountFragment : DaggerFragment() {

    lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        // Inflate the layout for this fragment

        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAccount)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAccount.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }

        return binding.root
    }

}
