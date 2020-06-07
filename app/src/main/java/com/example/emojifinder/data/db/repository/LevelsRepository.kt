package com.example.emojifinder.data.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.emojifinder.data.db.local.converter.asEmojiShopModel
import com.example.emojifinder.data.db.local.converter.asLocalModel
import com.example.emojifinder.data.db.local.converter.asSmallLevelUI
import com.example.emojifinder.data.db.local.room.database.LevelsDatabase
import com.example.emojifinder.data.db.remote.api.FirebaseLevels
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class LevelsRepository  @Inject constructor(
    private val firebaseCategories: FirebaseLevels,
    private val database: LevelsDatabase
){

    private val _isSimilarList = MutableLiveData<Boolean>()
    val isSimilarList : LiveData<Boolean>
        get() = _isSimilarList

    val levels : LiveData<List<SmallLevelModel>> = Transformations.map(database.levelDao().getLevels()){
        it.asSmallLevelUI()
    }

    fun getEmojis(title: String) : LiveData<List<EmojiShopModel>>
            = Transformations.map(database.emojisDao().getEmojis(title)){
        it.asEmojiShopModel()
    }

    fun getCurrentLevelList(smallLevelModel: SmallLevelModel) =
        Transformations.map(database.emojisDao().getEmojis(smallLevelModel.title)){
        it.asEmojiShopModel()
    }

    suspend fun deleteAll(){
        withContext(Dispatchers.IO){
            database.emojisDao().deleteAll()
            database.levelDao().deleteAll()
        }
    }

    suspend fun removeLevel(title : String) {
        withContext(Dispatchers.IO){
            database.emojisDao().delete(title)
            database.levelDao().delete(title)
        }
    }

    suspend fun addLevel(level: List<EmojiShopModel>, levelModel: SmallLevelModel) {
        withContext(Dispatchers.IO){
            database.emojisDao().insertAll(level.asLocalModel())
            database.levelDao().insert(asLocalModel(levelModel))
            println(levelModel)
        }
    }

    suspend fun delete(title : String){
        withContext(Dispatchers.IO){
            database.emojisDao().delete(title)
            database.levelDao().delete(title)
        }
    }

    suspend fun hasDifferences(currentList: List<EmojiShopModel>, smallLevelModel: SmallLevelModel) {

        val listDiffer =
            database.emojisDao().getEmojis(smallLevelModel.title)

        val list = listDiffer.value!!

        Timber.d(list.toString())
        Timber.d(currentList.toString())

        if (list.isNotEmpty()) {
            val pairs = list.zip(currentList)
            for (pair in pairs) {
                if (pair.first.unicode != pair.second.unicode) {
                    handleStatus(true)
                    break
                }
            }
        } else {
            handleStatus(false)
        }
        handleStatus(false)
    }

    private suspend fun handleStatus(differ : Boolean){
        withContext(Dispatchers .Main){
            _isSimilarList.value = differ
        }
    }

    suspend fun sentLevel(level: List<EmojiShopModel>, smallLevelModel: SmallLevelModel?) {
        firebaseCategories.addFullLevel(level, smallLevelModel)
    }


}



