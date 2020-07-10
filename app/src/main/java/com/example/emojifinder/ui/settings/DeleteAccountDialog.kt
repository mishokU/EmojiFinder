package com.example.emojifinder.ui.settings

import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object DeleteAccountDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.delete_account_dialog)
        close()
    }

    fun open(){
        dialogView.show()
    }

    private fun close(){
        close = dialogView.findViewById(R.id.cancel_delete)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getDeleteAccountBtn() : MaterialButton {
        return dialogView.findViewById(R.id.delete_account_btn)
    }
}