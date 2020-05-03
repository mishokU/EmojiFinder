package com.example.emojifinder.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentCategotyGameBinding
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.LogInViewModel
import dagger.android.support.DaggerFragment
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.shared.utils.asCategoryModel
import javax.inject.Inject


class CategoryGameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel : CategoriesViewModel

    lateinit var adapter: CategoryRecyclerViewAdapter
    lateinit var binding : FragmentCategotyGameBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategotyGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        // Inflate the layout for this fragment
        initCategories()
        initViewModel()

        initLevel()

        return binding.root
    }

    private fun initLevel() {
        viewModel.gameCategory.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(CategoryGameFragmentDirections.actionCategotyGameFragmentToGameFragment(
                    it
                ))
                viewModel.gameFragmentComplete()
            }
        })
    }

    private fun initViewModel() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorMessage.visibility = View.INVISIBLE

                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorMessage.text = result.exception.message
                    }
                }
            }
        })
    }

    private fun initCategories() {
        adapter = CategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter.OnCategoryClickListener{
            viewModel.showGameFragment(it)
        })
        binding.categoriesList.adapter = adapter
    }

}
