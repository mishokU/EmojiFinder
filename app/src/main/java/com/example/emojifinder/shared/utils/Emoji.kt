package com.example.emojifinder.shared.utils

object Emoji {

    private val emojiList : ArrayList<CharSequence> = arrayListOf(
        "\uD83D\uDE07", "\uD83D\uDE09", "\uD83E\uDD11", "\uD83E\uDD76",
        "\uD83D\uDE08", "\uD83E\uDD24", "\uD83D\uDE13", "\uD83E\uDD20"
    )

    fun getEmojiByUnicode(unicode: Int) : String{
        return String(Character.toChars(unicode))
    }

    fun getRandomEmoji(): CharSequence {
        return emojiList.random()
    }
}