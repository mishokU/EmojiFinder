package com.example.emojifinder.ui.game.arcade

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.emojifinder.R
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object EndGameDialog {

    lateinit var dialogView : Dialog
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment

        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.statusBarColor = ContextCompat.getColor(fragment.requireContext(), R.color.main_color)
        // layout to display
        dialogView.setContentView(R.layout.end_arcade_game_layout)

        // set color transparent
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color));
    }

    fun open(score: Int){
        val scoreView = dialogView.findViewById<TextView>(R.id.game_score)
        val emos = dialogView.findViewById<TextView>(R.id.game_emos)

        if(score == 0){
            scoreView.text = "0"
            emos.text = "0"
        } else {
            scoreView.text = score.toString()
            emos.text = (score / 5).toString()
        }
        dialogView.show()
    }

    fun getRetryButton() : View {
        return dialogView.findViewById(R.id.try_again_btn)
    }

    fun getExitButton() : MaterialButton {
        return dialogView.findViewById(R.id.exit_arcade_btn)
    }
}