package com.example.emojifinder.ui.boxes

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.R
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ShopViewModel
import com.example.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

object EmojisBoxDialog {

    lateinit var dialogView : BaseDialog
    lateinit var adapter : LootBoxRecyclerViewAdapter
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){

        this.fragment = fragment
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.fragment_loot_boxes)
    }

    fun createRecyclerView(shopEmojis: ShopViewModel) {
        fetchEmojis(shopEmojis)
        showDialog()
    }

    private fun fetchEmojis(shopEmojis: ShopViewModel) {
        shopEmojis.emojisResponse.observe(fragment.viewLifecycleOwner, Observer {
            it?.let{
                when(it){
                    is Result.Success -> {
                        adapter.submitList(it.data)
                    }
                }
            }
        })
    }

    fun showDialog(){
        dialogView.show()
    }


}