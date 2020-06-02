package com.example.emojifinder.data.db.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "levels_table")
data class EmojiShopModelLocal(

    @PrimaryKey(autoGenerate = true)
    val id : Int,

    val unicode : String,
    val order : Int,
    val x : Int,
    val y : Int
)