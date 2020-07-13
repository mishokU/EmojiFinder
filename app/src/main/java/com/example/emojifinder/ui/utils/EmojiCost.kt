package com.example.emojifinder.ui.utils

import com.example.emojifinder.ui.shop.EmojiShopModel
import com.example.emojifinder.ui.shop.ShopEmojiDialog
import com.google.android.material.button.MaterialButton

object EmojiCost {

    fun getEmojiBuyCost(buy : MaterialButton) : Int {
        val cost = buy.text.toString().split(" ")
        return cost[0].toInt()
    }

    fun getEmojiSellCost(sell : MaterialButton) : Int {
        val cost = sell.text.toString().split(" ")
        return cost[2].toInt()
    }

    fun emojiCost(emoji: EmojiShopModel?): Int {
        return when(emoji?.text?.length){
            in 1..2 -> 80
            in 2..4 -> 100
            in 6..8 -> 120
            else -> 100
        }
    }

    fun emojiSellCost(emoji: EmojiShopModel?): CharSequence? {
        return when(emoji?.text?.length){
            in 1..2 -> "Sell for 80 Emos"
            in 2..4 -> "Sell for 180 Emos"
            in 6..8 -> "Sell for 220 Emos"
            in 8..10 -> "Sell for 280 Emos"
            in 10..12 -> "Sell for 320 Emos"
            else -> "Sell for 150 Emos"
        }
    }
}