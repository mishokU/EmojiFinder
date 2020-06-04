package com.example.emojifinder.data.db.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels_table")
data class EmojiShopModelLocal(

    @PrimaryKey(autoGenerate = true)
    val id : Int,

    val levelTitle : String,
    val unicode : String,
    val order : Int,
    val x : Int,
    val y : Int
)