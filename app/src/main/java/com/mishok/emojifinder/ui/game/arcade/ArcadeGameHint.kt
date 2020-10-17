package com.mishok.emojifinder.ui.game.arcade

import android.widget.TextView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ArcadeGameHint  {


    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)

        dialogView.setContentView(R.layout.start_game_layout)

        val hint = dialogView.findViewById<TextView>(R.id.hintText)
        hint.text = hint.resources.getString(R.string.arcade_game_hint)
    }

    fun show() {
        dialogView.show()
    }

    fun getStartGameButton() : MaterialButton {
        return dialogView.findViewById(R.id.start_game_btn)
    }

}