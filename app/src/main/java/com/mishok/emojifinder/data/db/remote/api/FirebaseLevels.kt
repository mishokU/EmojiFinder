package com.mishok.emojifinder.data.db.remote.api

import android.graphics.Bitmap
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.categories.SmallLevelModel

interface FirebaseLevels {
    suspend fun fetchLevels(): Result<List<SmallLevelModel>>
    suspend fun fetchLevel(title: String?): Result<List<EmojiShopModel>>
    suspend fun addFullLevel(level: List<EmojiShopModel>, smallLevelModel: SmallLevelModel?, image: Bitmap?)
    suspend fun hasThisTitle(title: String): Boolean
    suspend fun hasThisTitleInUserLevels(title: String): Boolean
}