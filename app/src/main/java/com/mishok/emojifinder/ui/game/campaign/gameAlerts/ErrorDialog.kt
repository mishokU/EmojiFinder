package com.mishok.emojifinder.ui.game.campaign.gameAlerts

import android.view.View
import android.widget.TextView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

object ErrorDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.setContentView(R.layout.error_dialog_layout)

        setOkeyButton()
    }

    fun open(){
        dialogView.show()
    }

    private fun setOkeyButton() {
        val okeyButton = dialogView.findViewById<View>(R.id.okey_btn)
        okeyButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun setErrorText(error: String?) {
        open()
        val errorText = dialogView.findViewById<TextView>(R.id.errorText)
        errorText.text = error
    }



}