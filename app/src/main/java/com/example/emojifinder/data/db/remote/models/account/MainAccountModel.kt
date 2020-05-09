package com.example.emojifinder.data.db.remote.models.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainAccountModel(
    val email : String,
    val login : String,
    val password : String,
    val avatar : String,
    val score : Int
) : Parcelable {
    constructor() : this("", "", "", "", 0)
}
