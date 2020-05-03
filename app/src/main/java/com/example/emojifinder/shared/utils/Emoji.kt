package com.example.emojifinder.shared.utils

object Emoji {

    fun getEmojiByUnicode(unicode : Int) : String{
        return String(Character.toChars(unicode))
    }
}