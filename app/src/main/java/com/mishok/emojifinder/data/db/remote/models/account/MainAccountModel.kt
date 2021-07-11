package com.mishok.emojifinder.data.db.remote.models.account

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainAccountModel(
    val email : String,
    val login : String,
    val password : String,
    val avatar : String,
    val score : Int,
    val vip : Boolean,
    var place: Int = 0
) : Parcelable {
    constructor() : this("", "", "", "", 0, false, 0)
}
