package com.example.emojifinder.ui.help

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Help {ACCOUNT, GAME, OTHER}

@Parcelize
data class HelpModel(
    val id : Int,
    val title : String,
    val type : Help
) : Parcelable {
    constructor() : this(0, "", Help.OTHER)
}