package com.example.emojifinder.shared.utils

object Emoji {

    private val emojiList : ArrayList<CharSequence> = arrayListOf(
        "\uD83D\uDE02","\uD83D\uDE2D","\uD83E\uDD7A","\uD83E\uDD23",
        "❤️","✨","\uD83D\uDE0D","\uD83D\uDE4F","\uD83D\uDE0A",
        "\uD83E\uDD70","\uD83D\uDC4D","\uD83D\uDC95","\uD83E\uDD14"
    )

    fun getEmojiByUnicode(unicode: Int) : String{
        return String(Character.toChars(unicode))
    }

    fun getEmojiByString(cs : CharSequence) : CharSequence {
        return cs
    }

    fun getRandomEmoji(): CharSequence {
        return emojiList.random()
    }
}