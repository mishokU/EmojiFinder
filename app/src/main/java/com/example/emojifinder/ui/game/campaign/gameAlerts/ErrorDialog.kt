package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import dagger.android.support.DaggerFragment

object ErrorDialog {

    lateinit var dialogView : Dialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        dialogView.setContentView(R.layout.error_dialog_layout)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

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