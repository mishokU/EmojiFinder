package com.example.emojifinder.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentCategotyGameBinding
import com.example.emojifinder.domain.viewModels.CategoriesViewModel
import com.example.emojifinder.domain.viewModels.LogInViewModel
import dagger.android.support.DaggerFragment
import com.example.emojifinder.domain.result.Result
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

        initCategories()
        initViewModel()
        initBackButton()
        initLevel()

        return binding.root
    }

    private fun initBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.toolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.emoji_finder_levels)

        binding.toolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }


    private fun initLevel() {
        viewModel.gameCategory.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(CategoryGameFragmentDirections
                    .actionCategotyGameFragmentToGameFragment(it, adapter.currentList.toTypedArray())
                )
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
                        binding.retryLoadLevelsBtn.visibility = View.INVISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorMessage.visibility = View.INVISIBLE

                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorMessage.text = result.exception.message
                        binding.retryLoadLevelsBtn.visibility = View.VISIBLE
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
