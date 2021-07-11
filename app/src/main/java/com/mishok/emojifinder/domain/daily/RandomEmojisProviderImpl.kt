package com.mishok.emojifinder.domain.daily

import com.mishok.emojifinder.ui.shop.EmojiShopModel
import javax.inject.Inject

class RandomEmojisProviderImpl @Inject constructor(): RandomEmojisProvider {
    override var randomEmojis: List<EmojiShopModel> = emptyList()
}