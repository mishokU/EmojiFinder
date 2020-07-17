package com.example.emojifinder.ui.utils

import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.android.material.button.MaterialButton

object EmojiCost {

    fun getEmojiBuyCost(buy : MaterialButton) : Int {
        val cost = buy.text.toString().split(" ")
        return cost[0].toInt()
    }

    fun getEmojiSellCost(sell : MaterialButton) : Int {
        val cost = sell.text.toString().split(" ")
        return cost[0].toInt()
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
            in 1..2 -> "70"
            in 2..4 -> "90"
            in 6..8 -> "100"
            else -> "90"
        }
    }
}