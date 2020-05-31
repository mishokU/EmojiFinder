package com.example.emojifinder.ui.constructor

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.ViewModelFactory
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentLevelConstructorBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ConstructorViewModel
import com.example.emojifinder.domain.viewModels.ShopViewModel
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LevelConstructorFragment : DaggerFragment() {

    private lateinit var binding : FragmentLevelConstructorBinding
    private lateinit var allEmojisAdapter : AllEmojisRecyclerViewAdapter
    private lateinit var levelAdapter : LevelConstructorRecyclerViewAdapter


    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel : ConstructorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLevelConstructorBinding.inflate(inflater)

        setBackButton()

        initAllEmojisAdapter()
        initConstructorAdapter()

        getAllEmojisFromJson()
        getLevelEmojis()


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as DaggerAppCompatActivity).menuInflater.inflate(R.menu.constructor_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initConstructorAdapter() {
        levelAdapter = LevelConstructorRecyclerViewAdapter(LevelConstructorRecyclerViewAdapter.OnEmojiClickListener{

        })
        binding.constructorLevelRv.adapter = levelAdapter
    }

    private fun initAllEmojisAdapter() {
        allEmojisAdapter = AllEmojisRecyclerViewAdapter(AllEmojisRecyclerViewAdapter.OnEmojiClickListener {

        })
        binding.allEmojisConstructor.adapter = allEmojisAdapter
    }

    private fun getAllEmojisFromJson() {
        viewModelShop = injectViewModel(viewModelFactoryShop)
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                        binding.levelProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.levelProgressBar.visibility = View.INVISIBLE
                        allEmojisAdapter.submitList(it.data)
                    }
                    is Result.Error -> {

                    }
                }
            }
        })
    }

    private fun hideGrid(){

    }

    private fun showGrid(){

    }

    private fun getLevelEmojis() {
        viewModel = injectViewModel(viewModelFactory)
        viewModel.constructorLevelResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        levelAdapter.submitList(it.data)
                    }
                }
            }
        })
    }

    private fun setBackButton() {
        ((activity) as AppCompatActivity).setSupportActionBar(binding.constructorToolbar)
        ((activity) as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ((activity) as AppCompatActivity).supportActionBar?.title = ""

        binding.constructorToolbar.setNavigationOnClickListener {
            this.findNavController().navigateUp()
        }
    }

}
