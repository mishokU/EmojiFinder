package com.example.emojifinder.ui.boxes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.R
import com.example.emojifinder.core.di.utils.injectViewModel
import com.example.emojifinder.databinding.FragmentLootBoxesBinding
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class LootBoxesFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactoryShop: ViewModelProvider.Factory
    lateinit var viewModelShop : ShopViewModel

    lateinit var binding : FragmentLootBoxesBinding
    lateinit var adapter : LootBoxRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLootBoxesBinding.inflate(inflater)

        viewModelShop = injectViewModel(viewModelFactoryShop)

        binding.lifecycleOwner = this

        initAdapter()
        initShopViewModel()

        return binding.root
    }

    private fun initAdapter() {
        adapter = LootBoxRecyclerViewAdapter(LootBoxRecyclerViewAdapter.OnShopItemClickListener{

        })
        binding.chestRv.adapter = adapter
    }

    private fun initShopViewModel() {
        viewModelShop.loadEmojisFromJson()
        viewModelShop.emojisResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is Result.Loading -> {
                    //    binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                      //  binding.progressBar.visibility = View.INVISIBLE

                        adapter.submitList(it.data)

                        //generateGroupChips(it.data)
                    }
                    is Result.Error -> {
                    //    binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }


}
