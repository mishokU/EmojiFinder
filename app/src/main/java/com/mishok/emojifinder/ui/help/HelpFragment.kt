package com.mishok.emojifinder.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.FragmentHelpBinding
import dagger.android.support.DaggerFragment

class HelpFragment : DaggerFragment() {

    private lateinit var binding : FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpBinding.inflate(inflater)
        initQuestionsButton()
        initFab()
        return binding.root
    }

    private fun initQuestionsButton() {
        binding.howToDeleteAccount.setOnClickListener {
            navigate(binding.howToDeleteAccountText.text.toString(), Help.ACCOUNT)
        }
        binding.howToDisableAdds.setOnClickListener {
            navigate(binding.howToDisableAddsText.text.toString(), Help.OTHER)
        }
        binding.howToPlay.setOnClickListener {
            navigate(binding.howToPlayText.text.toString(), Help.GAME)
        }

        binding.forgetPassword.setOnClickListener {
            navigate(binding.forgetPasswordText.text.toString(), Help.ACCOUNT)
        }
        binding.badUserName.setOnClickListener {
            navigate(binding.badUserNameText.text.toString(), Help.ACCOUNT)
        }
        binding.statisticDoNotWrite.setOnClickListener {
            navigate(binding.statisticDoNotWriteText.text.toString(), Help.GAME)
        }

        binding.whatIsEmos.setOnClickListener {
            navigate(binding.whatIsEmosText.text.toString(), Help.OTHER)
        }

        binding.howToCombine.setOnClickListener {
            navigate(binding.howToCombineText.text.toString(), Help.OTHER)
        }
    }

    private fun navigate(text: String, account: Help) {
        this.findNavController().navigate(HelpFragmentDirections
            .actionHelpFragmentToReadQAFragment(
            HelpModel(
                id = 0,
                title = text,
                type = account
            )
        ))
    }

    private fun initFab() {
        binding.writeQuestion.setOnClickListener {
            this.findNavController().navigate(R.id.writeQuestionFragment)
        }
    }
}
