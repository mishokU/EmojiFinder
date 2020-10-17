package com.mishok.emojifinder.ui.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.Toast
import com.mishok.emojifinder.R
import com.mishok.emojifinder.databinding.FragmentWriteQuestionBinding
import com.mishok.emojifinder.domain.auth.CheckOnValid
import com.mishok.emojifinder.ui.utils.CustomTextWatcher
import dagger.android.support.DaggerFragment

class WriteQuestionFragment : DaggerFragment() {

    private lateinit var binding: FragmentWriteQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWriteQuestionBinding.inflate(inflater)

        initEditTextsWatcher()
        initSentBtn()

        return binding.root
    }

    private fun initSentBtn() {
        binding.sentQuestionBtn.setOnClickListener {
            sentMessage()
        }
    }

    private fun initEditTextsWatcher() {
        binding.userEmail.addTextChangedListener(editTextWatcher)
    }

    private fun sentMessage() {
        if (binding.userName.text.toString().isNotEmpty() &&
            binding.userEmail.text.toString().isNotEmpty() &&
            CheckOnValid.isEmailValid(binding.userEmail) &&
            binding.message.text.toString().isNotEmpty()
        ) {
            openMessageIntent(
                binding.userName.text.toString(),
                binding.userEmail.text.toString(),
                binding.message.text.toString()
            )
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.fill_all_fields),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openMessageIntent(
        userName: String,
        userEmail: String,
        message: String
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$toEmail"))
        intent.putExtra(Intent.EXTRA_SUBJECT, userName)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(intent)
    }

    private val editTextWatcher = object : CustomTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            CheckOnValid.isEmailValid(binding.userEmail)
        }
    }

    companion object {
        const val toEmail = "usov.misha@gmail.com"
    }
}
