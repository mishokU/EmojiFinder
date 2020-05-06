package com.example.emojifinder.data.db.remote.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiModel(
    val unicode : String,
    val order : Int,
    val x : Int,
    val y : Int
) : Parcelable {
    constructor() : this("", 0,0,0)
}