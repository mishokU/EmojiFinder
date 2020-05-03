package com.example.emojifinder.ui.categories

import android.os.Parcelable
import com.example.emojifinder.data.db.remote.models.EmojiModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryModel (
     val id : Int?,
     val title : String?,
     val emojis : EmojiModel
) : Parcelable {
    constructor() : this(0,"", EmojiModel())
}