package com.example.emojifinder.ui.boxes

import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object WinEmojiDialog {

    lateinit var dialogView: BaseDialog
    lateinit var fragment: DaggerFragment

    fun create(fragment: DaggerFragment) {
        this.fragment = fragment
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.won_emoji_dialog)

        takeEmojiBtn()
    }

    fun show(prize: EmojiShopModel) {
        setEmoji(prize)
        dialogView.show()
    }

    private fun setEmoji(prize: EmojiShopModel) {
        val emoji = dialogView.findViewById<EmojiAppCompatEditText>(R.id.win_emoji_et)
        emoji.setText(prize.text)
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