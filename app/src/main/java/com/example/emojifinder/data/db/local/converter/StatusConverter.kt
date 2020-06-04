package com.example.emojifinder.data.db.local.converter

import androidx.room.TypeConverter

class StatusConverter {

    @TypeConverter
    fun fromStatus(status : LevelStatus) : String {
        return when(status){
            LevelStatus.ACCEPTED -> "accepted"
            LevelStatus.DENIED -> "denied"
            LevelStatus.WAITING -> "waiting"
            LevelStatus.SAVED -> "saved"
        }
    }

    @TypeConverter
    fun toStatus(status : String) : LevelStatus {
        return when(status){
            "accepted" -> LevelStatus.ACCEPTED
            "denied" -> LevelStatus.DENIED
            "waiting" -> LevelStatus.WAITING
            "saved" -> LevelStatus.SAVED
            else -> LevelStatus.WAITING
        }
    }
}