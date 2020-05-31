package com.example.emojifinder.ui.categories

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SmallLevelModel (
     val id : Int?,
     val title : String?,
     val time : Int
) : Parcelable {
    constructor() : this(0,"", 0)
}