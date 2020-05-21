package com.example.emojifinder.ui.utils

import android.app.Dialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.emoji.widget.EmojiAppCompatEditText
import com.example.emojifinder.R
import com.example.emojifinder.ui.account.AccountAvatarFragment
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerFragment

object ShopEmojiDialog {

    lateinit var dialogView : Dialog
    lateinit var dialogUserView : Dialog

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

        getBuyButton().text = emojiCost(emoji)

        emojiAvatar.setText(emoji?.text)
        emojiName.text = emoji?.name
        emojiGroup.text = emoji?.group

        dialogView.show()

        getCancelButton(dialogView)
    }

    private fun emojiCost(emoji: EmojiShopModel?): CharSequence? {
        return when(emoji?.text?.length){
            in 1..2 -> "100 Emos"
            in 2..4 -> "200 Emos"
            in 6..8 -> "250 Emos"
            in 8..10 -> "300 Emos"
            in 10..12 -> "350 Emos"
            else -> "200 Emos"
        }
    }

    fun getBuyButton(): MaterialButton {
        return dialogView.findViewById(R.id.buy_emoji_btn)
    }

    private fun getSaleButton() : MaterialButton {
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
        dialogUserView = Dialog(fragment.requireContext(), R.style.CustomDialog)
        dialogUserView.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialogUserView.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // layout to display
        dialogUserView.setContentView(R.layout.full_emoji_user_item)

        // set color transparent
        dialogUserView.window!!.setBackgroundDrawable(fragment.resources.getDrawable(R.color.alert_background_color))

        val emojiAvatar = dialogUserView.findViewById<EmojiAppCompatEditText>(R.id.emoji_full_avatar)
        val emojiName = dialogUserView.findViewById<TextView>(R.id.emoji_full_name)
        val emojiGroup = dialogUserView.findViewById<TextView>(R.id.emoji_full_group)

        getSaleButton().text = emojiSellCost(emoji)

        emojiAvatar.setText(emoji?.text)
        emojiName.text = emoji?.name
        emojiGroup.text = emoji?.group

        dialogUserView.show()

        getCancelButton(dialogUserView)
        getNoButton(dialogUserView)
    }

    fun getEmojiBuyCost() : Int {
        val cost = getBuyButton().text.toString().split(" ")
        return cost[0].toInt()
    }

    fun getEmojiSellCost() : Int {
        val cost = getSaleButton().text.toString().split(" ")
        return cost[2].toInt()
    }

    private fun emojiSellCost(emoji: EmojiShopModel?): CharSequence? {
        return when(emoji?.text?.length){
            in 1..2 -> "Sell for 80 Emos"
            in 2..4 -> "Sell for 180 Emos"
            in 6..8 -> "Sell for 220 Emos"
            in 8..10 -> "Sell for 280 Emos"
            in 10..12 -> "Sell for 320 Emos"
            else -> "Sell for 150 Emos"
        }
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