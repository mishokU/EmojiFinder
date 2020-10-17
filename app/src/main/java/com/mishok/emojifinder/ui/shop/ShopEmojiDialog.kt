package com.mishok.emojifinder.ui.shop

import android.app.Dialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiAppCompatEditText
import com.mishok.emojifinder.R
import com.mishok.emojifinder.ui.account.AccountAvatarFragment
import com.mishok.emojifinder.ui.base.BaseDialog
import com.mishok.emojifinder.ui.utils.EmojiCost
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ShopEmojiDialog {

    lateinit var dialogView : BaseDialog
    lateinit var dialogUserView : BaseDialog

    fun showAlert(
        fragment: DaggerFragment,
        emoji: EmojiShopModel?
    ){
        dialogView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogView.setContentView(R.layout.full_emoji_shop_item)
        dialogView.setCancelable(true)

        val emojiAvatar = dialogView.findViewById<EmojiAppCompatEditText>(R.id.emoji_full_avatar)
        val emojiName = dialogView.findViewById<TextView>(R.id.emoji_full_name)
        val emojiGroup = dialogView.findViewById<TextView>(R.id.emoji_full_group)

        getBuyButton().text = EmojiCost.emojiCost(emoji).toString()

        emojiAvatar.setText(emoji?.text)
        emojiName.text = emoji?.name
        emojiGroup.text = emoji?.group

        dialogView.show()

        getCancelButton(
            dialogView
        )
    }

    fun getBuyButton(): MaterialButton {
        return dialogView.findViewById(R.id.buy_emoji_btn)
    }

    fun getSaleButton() : MaterialButton {
        val showAlert : MaterialButton = dialogUserView.findViewById(R.id.sale_emoji_btn)
        val approvePlace : LinearLayout = dialogUserView.findViewById(R.id.approve_place)
        showAlert.setOnClickListener {
            approvePlace.visibility = View.VISIBLE
        }
        return showAlert
    }

    fun getYesSaleButton() : MaterialButton{
        return dialogUserView.findViewById(R.id.yes_emoji_btn)
    }

    fun showUserAlert(fragment: AccountAvatarFragment, emoji: EmojiShopModel?) {
        dialogUserView = BaseDialog(fragment.requireContext(), R.style.CustomDialog)
        dialogUserView.setContentView(R.layout.full_emoji_user_item)

        val emojiAvatar = dialogUserView.findViewById<EmojiAppCompatEditText>(R.id.emoji_full_avatar)
        val emojiName = dialogUserView.findViewById<TextView>(R.id.emoji_full_name)
        val emojiGroup = dialogUserView.findViewById<TextView>(R.id.emoji_full_group)

        getSaleButton().text = EmojiCost.emojiSellCost(emoji)

        emojiAvatar.setText(emoji?.text)
        emojiName.text = emoji?.name
        emojiGroup.text = emoji?.group

        dialogUserView.show()

        getCancelButton(
            dialogUserView
        )
        getNoButton(
            dialogUserView
        )
    }

    private fun getNoButton(dialogView: Dialog){
        val cancel : MaterialButton = dialogView.findViewById(R.id.no_emoji_btn)
        cancel.setOnClickListener {
            dialogView.dismiss()
        }
    }

    private fun getCancelButton(dialogView: Dialog) {
        val cancel : ImageView = dialogView.findViewById(R.id.cancel_emoji_btn)
        cancel.setOnClickListener {
            dialogView.dismiss()
        }
    }
}