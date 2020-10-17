package com.mishok.emojifinder.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mishok.emojifinder.databinding.FragmentReadQABinding
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

        return binding.root
    }

    private fun getQuestionFromBundle() {
        question = ReadQAFragmentArgs.fromBundle(requireArguments()).Help
        binding.problemTitle.text = question.title
        binding.answerToProblem.text = AnswerHelper.getAnswer(requireContext(), question.title)
    }

    private fun initFab() {
//        binding.writeQuestion.setOnClickListener {
//            this.findNavController().navigate(R.id.writeQuestionFragment)
//        }
    }

}
