package com.example.emojifinder.ui.boxes

import android.app.Dialog
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.emojifinder.R
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.domain.viewModels.ShopViewModel
import dagger.android.support.DaggerFragment

object EmojisBoxDialog {

    lateinit var dialogView : Dialog
    lateinit var adapter : LootBoxRecyclerViewAdapter
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){

        this.fragment = fragment

        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        // layout to display
        dialogView.setContentView(R.layout.fragment_loot_boxes)

        // set color transparent
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color))
    }

    fun createRecyclerView(shopEmojis: ShopViewModel) {

        val recyclerView : RecyclerView = dialogView.findViewById(R.id.chest_rv)
        adapter = LootBoxRecyclerViewAdapter(LootBoxRecyclerViewAdapter.OnShopItemClickListener{

        })
        recyclerView.adapter = adapter

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