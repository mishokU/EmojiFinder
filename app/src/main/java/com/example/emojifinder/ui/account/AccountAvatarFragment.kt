package com.example.emojifinder.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentAccountAvatarBinding


class AccountAvatarFragment : Fragment() {

    lateinit var binding : FragmentAccountAvatarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountAvatarBinding.inflate(inflater)
        // Inflate the layout for this fragment

        setBackButton()

        return binding.root
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarAvatar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarAvatar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
