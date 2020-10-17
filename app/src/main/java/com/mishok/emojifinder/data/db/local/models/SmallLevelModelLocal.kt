package com.mishok.emojifinder.data.db.local.models

import androidx.room.*
import com.mishok.emojifinder.data.db.local.converter.LevelStatus
import com.mishok.emojifinder.data.db.local.converter.StatusConverter

@Entity(tableName = "small_levels_table")
data class SmallLevelModelLocal(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @TypeConverters(StatusConverter::class)
    val status: LevelStatus,
    val title: String,
    val time: Int,
    val url: String
)