package com.example.emojifinder.ui.boxes

import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.base.BaseDialog
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object NothingEmojiDialog {

    lateinit var dialogView: BaseDialog
    lateinit var adapter: LootBoxRecyclerViewAdapter
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