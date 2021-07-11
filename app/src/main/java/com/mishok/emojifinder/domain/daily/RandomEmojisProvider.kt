package com.mishok.emojifinder.domain.daily

import com.mishok.emojifinder.ui.shop.EmojiShopModel

interface RandomEmojisProvider {
    var randomEmojis : List<EmojiShopModel>
}