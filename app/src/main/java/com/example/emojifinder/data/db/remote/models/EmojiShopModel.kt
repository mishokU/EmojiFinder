package com.example.emojifinder.data.db.remote.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiShopModel(
    var unicode : String,
    var order : Int,
    val x : Int,
    val y : Int
) : Parcelable {
    constructor() : this("", -1,0,0)
}