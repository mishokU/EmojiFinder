package com.example.emojifinder.ui.help

import android.os.Bundle
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
        initQuestionsButton()
        initFab()
        return binding.root
    }

    private fun initQuestionsButton() {
        binding.howToDeleteAccount.setOnClickListener {
            navigate(binding.howToDeleteAccount.text.toString(), Help.ACCOUNT)
        }
        binding.howToDisableAdds.setOnClickListener {
            navigate(binding.howToDisableAdds.text.toString(), Help.OTHER)
        }
        binding.howToPlay.setOnClickListener {
            navigate(binding.howToPlay.text.toString(), Help.GAME)
        }

        binding.forgetPassword.setOnClickListener {
            navigate(binding.forgetPassword.text.toString(), Help.ACCOUNT)
        }
        binding.badUserName.setOnClickListener {
            navigate(binding.badUserName.text.toString(), Help.ACCOUNT)
        }
        binding.statisticDoNotWrite.setOnClickListener {
            navigate(binding.statisticDoNotWrite.text.toString(), Help.GAME)
        }

        binding.whatIsEmos.setOnClickListener {
            navigate(binding.whatIsEmos.text.toString(), Help.OTHER)
        }

        binding.howToCombine.setOnClickListener {
            navigate(binding.howToCombine.text.toString(), Help.OTHER)
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
