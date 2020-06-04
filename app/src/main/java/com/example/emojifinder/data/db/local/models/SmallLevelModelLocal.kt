package com.example.emojifinder.data.db.local.models

import android.os.Parcelable
import androidx.room.*
import com.example.emojifinder.data.db.local.converter.LevelStatus
import com.example.emojifinder.data.db.local.converter.StatusConverter
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "small_levels_table")
data class SmallLevelModelLocal (

     @PrimaryKey(autoGenerate = true)
     val id : Int,

     @TypeConverters(StatusConverter::class)
     val status : LevelStatus,
     val title : String,
     val time : Int
)