package com.example.emojifinder.ui.boxes

import com.example.emojifinder.R
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


    fun showDialog(){
        dialogView.show()
    }
}