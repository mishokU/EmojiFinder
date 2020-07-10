package com.example.emojifinder.ui.settings

import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object LogOutDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(true)
        dialogView.setContentView(R.layout.log_out_dialog)
        closeBtn()
    }

    fun open(){
        dialogView.show()
    }

    private fun closeBtn(){
        close = dialogView.findViewById(R.id.cancel_log_out)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getLogOutButton() : MaterialButton {
        return dialogView.findViewById(R.id.log_out_btn)
    }

}