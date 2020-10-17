package com.mishok.emojifinder.data.db.remote.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Point(
    val x : Float,
    val y : Float
) : Parcelable