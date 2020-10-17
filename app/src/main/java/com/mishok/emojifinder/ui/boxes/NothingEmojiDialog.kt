package com.mishok.emojifinder.ui.boxes

import android.widget.TextView
import androidx.emoji.widget.EmojiAppCompatEditText
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object NothingEmojiDialog {

    lateinit var dialogView: BaseDialog
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment) {
        this.fragment = fragment
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.won_emoji_dialog)

        takeEmojiBtn()
        setEmoji()
    }

    fun show() {
        dialogView.show()
    }

    private fun setEmoji() {
        val emoji = dialogView.findViewById<EmojiAppCompatEditText>(R.id.win_emoji_et)
        val title = dialogView.findViewById<TextView>(R.id.title_win_emoji)
        val take = dialogView.findViewById<MaterialButton>(R.id.roll_again_btn)
        take.text = "Ok"

        title.text = title.resources.getString(R.string.win_nothing)
        emoji.setText("\uD83D\uDE22")
    }

    private fun hide() {
        dialogView.dismiss()
    }

    private fun takeEmojiBtn() {
        val take = dialogView.findViewById<MaterialButton>(R.id.roll_again_btn)
        take.setOnClickListener {
            hide()
        }
    }
}