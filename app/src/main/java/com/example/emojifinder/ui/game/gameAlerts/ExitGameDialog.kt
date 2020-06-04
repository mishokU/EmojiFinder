package com.example.emojifinder.ui.game.gameAlerts

import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.emojifinder.R
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ExitGameDialog {

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
        dialogView.setContentView(R.layout.exit_game_layout)
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
        dialogView.window!!.attributes.windowAnimations = R.anim.fragment_fade_enter

    }

    fun open(){
        dialogView.show()
    }

    fun getGameExitButton() : MaterialButton {
        return dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getResumeGameButton() : MaterialButton {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}