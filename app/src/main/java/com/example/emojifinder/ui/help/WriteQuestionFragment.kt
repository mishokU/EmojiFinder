package com.example.emojifinder.ui.help

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.databinding.FragmentWriteQuestionBinding
import com.example.emojifinder.domain.auth.CheckOnValid
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.combine

class WriteQuestionFragment : DaggerFragment() {

    private lateinit var binding : FragmentWriteQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWriteQuestionBinding.inflate(inflater)

        initToolbar()
        initEditTextsWatcher()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun initEditTextsWatcher() {
        binding.userEmail.addTextChangedListener(EditTextWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as DaggerAppCompatActivity).menuInflater.inflate(R.menu.write_question_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when(item.itemId) {
            R.id.write_question_btn -> {
                sentMessage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    private fun checkPermissions() {

    }

    private fun sentMessage() {
        if(binding.userName.text.toString().isNotEmpty() &&
                binding.userEmail.text.toString().isNotEmpty() &&
                CheckOnValid.isEmailValid(binding.userEmail) &&
                binding.message.text.toString().isNotEmpty()){
            Toast.makeText(requireContext(),"Send", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(),resources.getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
        }
    }

    private fun initToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.helpToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.contact)

        binding.helpToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private val EditTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            CheckOnValid.isEmailValid(binding.userEmail)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}
