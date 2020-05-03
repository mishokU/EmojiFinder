package com.example.emojifinder.data.db.remote.models

data class LevelModel(
    val id : Int,
    val duration : Double,
    val emojies : ArrayList<EmojiModel>
)