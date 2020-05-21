package com.example.emojifinder.data.db.remote.models.account

data class EmojiShopModelFirebase(
    val id : Int,
    val category: String,
    val text: String,
    val codes: String,
    val group: String,
    val name: String,
    val subgroup: String
)