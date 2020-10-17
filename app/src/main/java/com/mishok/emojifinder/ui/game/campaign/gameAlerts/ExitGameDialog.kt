package com.mishok.emojifinder.ui.game.campaign.gameAlerts

import androidx.appcompat.widget.AppCompatImageButton
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

object ExitGameDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.exit_game_layout)
    }

    fun open(){
        dialogView.show()
    }

    fun getGameExitButton() : AppCompatImageButton {
        return dialogView.findViewById(R.id.exit_game_btn)
    }

    fun getResumeGameButton() : AppCompatImageButton {
        return dialogView.findViewById(R.id.resume_game_btn)
    }

}