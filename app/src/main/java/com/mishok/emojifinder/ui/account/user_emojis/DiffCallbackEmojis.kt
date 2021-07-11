package com.mishok.emojifinder.ui.account.user_emojis

import androidx.recyclerview.widget.DiffUtil
import com.mishok.emojifinder.ui.shop.EmojiShopModel

class DailyDiffCallbackEmojis : DiffUtil.ItemCallback<EmojiShopModel>() {

    override fun areItemsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
        return oldItem.id ==newItem.id
    }

    override fun areContentsTheSame(oldItem: EmojiShopModel, newItem: EmojiShopModel): Boolean {
        return oldItem == newItem
    }
}
