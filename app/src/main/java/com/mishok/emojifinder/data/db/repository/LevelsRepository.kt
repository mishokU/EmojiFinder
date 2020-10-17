package com.mishok.emojifinder.data.db.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mishok.emojifinder.data.db.local.converter.asEmojiShopModel
import com.mishok.emojifinder.data.db.local.converter.asLocalModel
import com.mishok.emojifinder.data.db.local.converter.asSmallLevelUI
import com.mishok.emojifinder.data.db.local.room.database.LevelsDatabase
import com.mishok.emojifinder.data.db.remote.api.FirebaseLevels
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LevelsRepository @Inject constructor(
    private val firebaseCategories: FirebaseLevels,
    private val database: LevelsDatabase
) {

    private val _isSimilarList = MutableLiveData<Boolean>()
    val isSimilarList: LiveData<Boolean>
        get() = _isSimilarList

    private val _hasTitle = MutableLiveData<Boolean>()
    val hasTitle: LiveData<Boolean>
        get() = _hasTitle

    val levels: LiveData<List<SmallLevelModel>> =
        Transformations.map(database.levelDao().getLevels()) {
            it.asSmallLevelUI()
        }

    fun getEmojis(title: String): LiveData<List<EmojiShopModel>> =
        Transformations.map(database.emojisDao().getEmojis(title)) {
            it.asEmojiShopModel()
        }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            database.emojisDao().deleteAll()
            database.levelDao().deleteAll()
        }
    }

    suspend fun removeLevel(title: String) {
        withContext(Dispatchers.IO) {
            database.emojisDao().delete(title)
            database.levelDao().delete(title)
        }
    }

    suspend fun addLevel(level: List<EmojiShopModel>, levelModel: SmallLevelModel) {
        withContext(Dispatchers.IO) {
            database.emojisDao().insertAll(level.asLocalModel())
            database.levelDao().insert(asLocalModel(levelModel))
            println(levelModel)
        }
    }

    suspend fun delete(title: String) {
        withContext(Dispatchers.IO) {
            database.emojisDao().delete(title)
            database.levelDao().delete(title)
        }
    }

    suspend fun sentLevel(
        level: List<EmojiShopModel>,
        smallLevelModel: SmallLevelModel?,
        image: Bitmap?
    ) {
        firebaseCategories.addFullLevel(level, smallLevelModel, image)
    }

    fun hasTitleComplete() {
        _hasTitle.value = null
    }

    suspend fun checkOnSameTitle(title: String) {
        var result = firebaseCategories.hasThisTitle(title)
        if(!result) {
           result = firebaseCategories.hasThisTitleInUserLevels(title)
        }
        withContext(Dispatchers.Main) {
            _hasTitle.value = result
        }
    }
}



