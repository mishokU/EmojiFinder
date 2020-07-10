package com.example.emojifinder.ui.categories

import android.os.Parcelable
import com.example.emojifinder.data.db.local.converter.LevelStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SmallLevelModel(
    val id: Int,
    val status: LevelStatus,
    val title: String,
    val time: Int,
    val url : String
) : Parcelable {
    constructor() : this(0, LevelStatus.WAITING, "", 0, "")
}