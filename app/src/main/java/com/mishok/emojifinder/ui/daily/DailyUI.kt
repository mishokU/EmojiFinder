package com.mishok.emojifinder.ui.daily

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DailyUI (
    var day : Int
) : Parcelable