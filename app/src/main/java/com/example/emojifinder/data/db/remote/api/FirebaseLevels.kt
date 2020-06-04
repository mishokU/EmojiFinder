package com.example.emojifinder.data.db.remote.api

import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel

interface FirebaseLevels {
    suspend fun fetchLevels() : Result<List<SmallLevelModel>>
    suspend fun fetchLevel(title : String?) : Result<List<EmojiShopModel?>>
    suspend fun addFullLevel(
        level: List<EmojiShopModel>,
        smallLevelModel: SmallLevelModel?
    )
}