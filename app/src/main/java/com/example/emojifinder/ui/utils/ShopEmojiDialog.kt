package com.example.emojifinder.ui.utils

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ShopEmojiDialog {

    lateinit var dialogView : Dialog

    fun showAlert(
        fragment: DaggerFragment,
        emoji: EmojiShopModel?
    ){
        dialogView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // layout to display
        dialogView.setContentView(R.layout.full_emoji_shop_item)

        // set color transparent
        dialogView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color))

        val emojiAvatar = dialogView.findViewById<EmojiAppCompatEditText>(R.id.emoji_full_avatar)
        val emojiName = dialogView.findViewById<TextView>(R.id.emoji_full_name)
        val emojiGroup = dialogView.findViewById<TextView>(R.id.emoji_full_group)

        emojiAvatar.setText(emoji?.text)
        emojiName.text = emoji?.name
        emojiGroup.text = emoji?.group

        dialogView.show()

        getCancelButton()
    }

    fun getBuyButton() : MaterialButton {
        return dialogView.findViewById(R.id.buy_emoji_btn)
    }

    private fun getCancelButton() {
        val cancel : ImageView = dialogView.findViewById(R.id.cancel_emoji_btn)
        cancel.setOnClickListener {
            dialogView.dismiss()
        }
    }
}