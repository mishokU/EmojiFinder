package com.example.emojifinder.ui.levels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.ViewModelFactory
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentYourLevelsBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ConstructorViewModel
import com.example.emojifinder.ui.account.UserLevelRecyclerViewAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class YourLevelsFragment : DaggerFragment() {

    private lateinit var binding : FragmentYourLevelsBinding
    private lateinit var adapter : UserLevelsRecyclerViewAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel : ConstructorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourLevelsBinding.inflate(inflater)

        initToolbar()

        DeleteLevelDialog.create(this)

        initDeleteDialogButton()
        initAdapter()
        initUserLevelsViewModel()
        goToConstructor()

        return binding.root
    }

    private fun initToolbar() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbarYourLevels)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarYourLevels.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun initDeleteDialogButton() {
        DeleteLevelDialog.getDeleteLevelBtn().setOnClickListener {
            viewModel.deleteLevel(DeleteLevelDialog.level.title)
            DeleteLevelDialog.dialogView.dismiss()
        }
    }

    private fun goToConstructor() {
        binding.constructor.setOnClickListener {
            this.findNavController().navigate(R.id.levelConstructorFragment)
        }
    }

    private fun initAdapter() {
        adapter = UserLevelsRecyclerViewAdapter(UserLevelsRecyclerViewAdapter.OnLevelClickListener{
            this.findNavController().navigate(YourLevelsFragmentDirections.actionYourLevelsFragmentToLevelConstructorFragment(
                it!!
            ))
        }, UserLevelsRecyclerViewAdapter.OnDeleteClickListener {
            DeleteLevelDialog.open(it)
        })
        binding.yourLevelsRv.adapter = adapter
    }


    private fun initUserLevelsViewModel() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.userLevels.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

}
