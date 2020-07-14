package com.example.emojifinder.ui.shop

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmojiShopModel(
    val id : Int,
    val category: String,
    @SerializedName(value = "char")
    var text: String,
    val codes: String,
    var group: String,
    val name: String,
    val subgroup: String
) : Parcelable {
    constructor() : this(0,"","","","","","")
}