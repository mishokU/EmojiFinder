package com.example.emojifinder.ui.boxes

import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object TicketEmojiDialog {

    lateinit var dialogView: BaseDialog
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment) {
        this.fragment = fragment
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.ticket_emoji_dialog)

        takeEmojiBtn()
        setEmoji()
    }

    fun show() {
        dialogView.show()
    }

    private fun setEmoji() {
        val emoji = dialogView.findViewById<EmojiAppCompatEditText>(R.id.ticket_emoji_et)
        emoji.setText("\uD83C\uDF9F️")
    }

    private fun hide() {
        dialogView.dismiss()
    }

    private fun takeEmojiBtn() {
        val take = dialogView.findViewById<MaterialButton>(R.id.take_ticket_btn)
        take.setOnClickListener {
            hide()
        }
    }
}