package com.example.emojifinder.ui.categories

import android.os.Parcelable
import com.example.emojifinder.data.db.remote.models.EmojiModel
import com.example.emojifinder.data.db.remote.models.Point
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SmallLevelModel (
     val id : Int?,
     val title : String?
) : Parcelable {
    constructor() : this(0,"")
}