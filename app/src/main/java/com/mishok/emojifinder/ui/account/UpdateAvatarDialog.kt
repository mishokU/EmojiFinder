package com.mishok.emojifinder.ui.account

import androidx.emoji.widget.EmojiAppCompatEditText
import com.google.android.material.button.MaterialButton
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.base.BaseDialog
import dagger.android.support.DaggerFragment

object UpdateAvatarDialog {

    lateinit var dialogView : BaseDialog
    private lateinit var fragment: DaggerFragment
    private lateinit var close : MaterialButton
    private lateinit var emoji : EmojiAppCompatEditText

    fun create(fragment: DaggerFragment){
        this.fragment = fragment
        createDialog()
    }

    private fun createDialog() {
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setCancelable(true)
        dialogView.setContentView(R.layout.change_avatar_dialog)
    }

    fun getUpdateAvatarBtn() : MaterialButton {
        return dialogView.findViewById(R.id.update_avatar_btn)
    }

    fun show(text: String?) {
        emoji = dialogView.findViewById(R.id.avatar_emoji)
        emoji.setText(text)
        dialogView.show()
    }

    fun getEmoji(): String {
        return emoji.text.toString()
    }
}