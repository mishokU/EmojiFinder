package com.mishok.emojifinder.ui.game.arcade

import android.view.View
import android.widget.TextView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

object EndGameDialog {

    lateinit var dialogView : BaseDialog
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment

        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.end_arcade_game_layout)
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
        return dialogView.findViewById(R.id.try_arcade_game_btn)
    }

    fun getExitButton() : View {
        return dialogView.findViewById(R.id.exit_arcade_btn)
    }
}