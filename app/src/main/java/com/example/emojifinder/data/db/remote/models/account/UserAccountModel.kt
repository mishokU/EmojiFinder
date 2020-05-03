package com.example.emojifinder.data.db.remote.models.account

import com.example.emojifinder.data.db.remote.models.LevelModel

data class UserAccountModel(
    val mainInfo : MainAccountModel,
    val values : AccountValuesModel,
    val levelInfo : LevelModel,
    val rating : Int
)