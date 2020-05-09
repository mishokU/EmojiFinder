package com.example.emojifinder.data.db.remote.models.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserLevelStatistics(
    val id : Int,
    val time : String,
    val mistakes : Int,
    val score : Int,
    val max_score : Int,
    val title : String,
    val result : String
) : Parcelable {
    constructor() : this(
        0,"",0,0,0,"",""
    )
}