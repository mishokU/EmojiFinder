package com.example.emojifinder.ui.daily

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DailyUI (
    val day : Int
) : Parcelable