package com.mishok.emojifinder.ui.account.user_emojis

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.mishok.emojifinder.ui.shop.EmojiShopModel

class UserEmojisAdapter(emoji: String, callback: (EmojiShopModel) -> Unit
): AsyncListDifferDelegationAdapter<EmojiShopModel>(DailyDiffCallbackEmojis()) {

    init {
        delegatesManager.addDelegate(UserEmojiDelegate(emoji, callback))
    }
}