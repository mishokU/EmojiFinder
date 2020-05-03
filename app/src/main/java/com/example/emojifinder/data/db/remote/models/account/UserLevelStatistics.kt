package com.example.emojifinder.data.db.remote.models.account

data class UserLevelStatistics(
    val id : Int,
    val time : String,
    val mistakes : Int,
    val score : Int,
    val result : String
)