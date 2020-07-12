package com.example.emojifinder.ui.game.campaign.gameAlerts

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.main.MainActivity
import java.util.*

object ShowStartGameButton {

    lateinit var dialogView: BaseDialog

    fun create(
        activity: Context,
        level: SmallLevelModel
    ) {
        dialogView = BaseDialog(activity, R.style.CustomDialog)
        dialogView.setCancelable(false)
        dialogView.setContentView(R.layout.game_start_alert_dialog)

        setEmoji()
        setGameTitle(level)
    }

    private fun setEmoji() {
        val emoji = dialogView.findViewById<EmojiAppCompatEditText>(R.id.start_emoji)
        emoji.setText("\uD83D\uDE00")
    }

    @SuppressLint("SetTextI18n")
    private fun setGameTitle(level: SmallLevelModel) {
        val title: TextView = dialogView.findViewById(R.id.level_title)
        val first: String = level.title[0].toString().toUpperCase(Locale.ROOT)
        val all: String? = level.title.drop(1)
        title.text = first + all
    }

    fun show() {
        dialogView.show()
    }

    fun hide() {
        dialogView.dismiss()
    }

    fun showError(message: String?) {

    }
}