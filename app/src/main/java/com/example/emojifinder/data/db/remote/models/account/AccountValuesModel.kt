package com.example.emojifinder.data.db.remote.models.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountValuesModel(
    var boxes : Int,
    var emos : Int,
    var emojis : Int
) : Parcelable {
    constructor() : this(0,0,0)
}
