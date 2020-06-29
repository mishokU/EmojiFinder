package com.example.emojifinder.data.db.local.models

enum class Daily {EMOS, BOX, EMOJI}

data class DailyModel(
    val id : Int,
    val type : Daily,
    val cost : Int
)