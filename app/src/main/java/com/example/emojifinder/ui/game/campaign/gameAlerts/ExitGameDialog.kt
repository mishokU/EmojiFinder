package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.widget.Switch
import androidx.appcompat.widget.AppCompatImageButton
import com.example.emojifinder.R
import com.example.emojifinder.domain.prefs.SettingsPrefs
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.main.MainActivity
import com.google.android.material.button.MaterialButton
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