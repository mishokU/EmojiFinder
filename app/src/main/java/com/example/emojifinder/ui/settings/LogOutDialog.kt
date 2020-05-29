package com.example.emojifinder.ui.settings

import android.app.Dialog
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object LogOutDialog {

    lateinit var dialogView : Dialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        dialogView.setContentView(R.layout.log_out_dialog)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color))
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

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