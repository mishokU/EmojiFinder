package com.mishok.emojifinder.ui.levels

import com.google.android.material.button.MaterialButton
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import dagger.android.support.DaggerFragment

object DeleteLevelDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton
    lateinit var level : SmallLevelModel

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.delete_level_dialog)

        close()
    }

    fun open(it : SmallLevelModel){
        level = it
        dialogView.show()
    }

    private fun close(){
        close = dialogView.findViewById(R.id.cancel_delete_level)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getDeleteLevelBtn() : MaterialButton {
        return dialogView.findViewById(R.id.delete_level_btn)
    }
}