package com.example.emojifinder.ui.constructor.dialogs

import android.app.Dialog
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ResetLevelDialog {

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
        dialogView.setContentView(R.layout.reset_level_dialog)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

        close()
    }

    fun open(){
        dialogView.show()
    }

    private fun close(){
        close = dialogView.findViewById(R.id.cancel_reset_level)
        close.setOnClickListener {
            dialogView.dismiss()
        }
    }

    fun getResetLevelBtn() : MaterialButton {
        return dialogView.findViewById(R.id.reset_level_btn)
    }
}