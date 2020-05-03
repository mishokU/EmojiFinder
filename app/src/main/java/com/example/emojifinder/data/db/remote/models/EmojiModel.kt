package com.example.emojifinder.data.db.remote.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiModel(
    val id : Int,
    val code : String,
    val emoji : String,
    val unicode : String,
    val category : String,
    val position : Point
) : Parcelable