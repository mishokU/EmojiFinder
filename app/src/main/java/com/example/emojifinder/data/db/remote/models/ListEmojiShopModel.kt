package com.example.emojifinder.data.db.remote.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListEmojiShopModel(
    val level : List<EmojiShopModel>
) : Parcelable {
    constructor() : this(emptyList())
}