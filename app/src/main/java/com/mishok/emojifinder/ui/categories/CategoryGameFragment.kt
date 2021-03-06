package com.mishok.emojifinder.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mishok.emojifinder.domain.result.Result
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mishok.emojifinder.core.di.utils.injectViewModel
import com.mishok.emojifinder.databinding.FragmentCategotyGameBinding
import com.mishok.emojifinder.domain.viewModels.CategoriesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class CategoryGameFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CategoriesViewModel

    lateinit var adapter: CategoryRecyclerViewAdapter
    lateinit var binding: FragmentCategotyGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategotyGameBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initCategories()
        initViewModel()
        initLevel()
        initPlayButton()

        return binding.root
    }

    private fun initPlayButton() {
        binding.playLevelBtn.setOnClickListener {
            viewModel.showGameFragment(adapter.items[adapter.currentItem])
        }
    }

    private fun initLevel() {
        viewModel.gameCategory.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    CategoryGameFragmentDirections
                        .actionCategotyGameFragmentToGameFragment(
                            it,
                            adapter.items.toTypedArray()
                        )
                )
                viewModel.gameFragmentComplete()
            }
        })
    }

    private fun initViewModel() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.retryLoadLevelsBtn.visibility = View.INVISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorMessage.visibility = View.INVISIBLE
                        @Suppress("UNCHECKED_CAST")
                        adapter.setLevels(result.data as List<SmallLevelModel>)
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
        adapter = CategoryRecyclerViewAdapter(CategoryRecyclerViewAdapter.OnCategoryClickListener {
                viewModel.showGameFragment(it)
            })
        binding.categoriesList.initialize(adapter)
    }

}
