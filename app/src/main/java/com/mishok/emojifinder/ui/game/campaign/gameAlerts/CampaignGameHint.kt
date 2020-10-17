package com.mishok.emojifinder.ui.game.campaign.gameAlerts

import android.annotation.SuppressLint
import android.widget.TextView
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object CampaignGameHint  {


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
    }

    fun show(level : SmallLevelModel) {
        setGameTime(level.time)
        dialogView.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setGameTime(time: Int) {
        val hint = dialogView.findViewById<TextView>(R.id.hintText)
        hint.text = hint.resources.getString(R.string.game_hint) + " " + time + " " + hint.resources.getString(R.string.sec)
    }


    fun getStartGameButton() : MaterialButton {
        return dialogView.findViewById(R.id.start_game_btn)
    }

}