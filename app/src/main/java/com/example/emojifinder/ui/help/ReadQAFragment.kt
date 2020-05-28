package com.example.emojifinder.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentReadQABinding
import dagger.android.support.DaggerFragment


class ReadQAFragment : DaggerFragment() {

    private lateinit var binding : FragmentReadQABinding
    private lateinit var question : HelpModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadQABinding.inflate(inflater)
        // Inflate the layout for this fragment

        getQuestionFromBundle()

        initFab()
        initToolbar()

        return binding.root
    }

    private fun getQuestionFromBundle() {
        question = ReadQAFragmentArgs.fromBundle(requireArguments()).Help
        binding.problemTitle.text = question.title
        binding.answerToProblem.text = AnswerHelper.getAnswer(requireContext(), question.title)
    }

    private fun initFab() {
        binding.writeQuestion.setOnClickListener {
            this.findNavController().navigate(R.id.writeQuestionFragment)
        }
    }

    private fun initToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.readQaToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.answer)

        binding.readQaToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
