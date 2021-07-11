package com.mishok.emojifinder.domain.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.local.fake.LevelConstructorService
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.data.db.repository.LevelsRepository
import com.mishok.emojifinder.domain.daily.RandomEmojisProvider
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.*
import javax.inject.Inject

class ConstructorViewModel @Inject constructor(
    private val levelConstructorService: LevelConstructorService,
    private val levelsRepository: LevelsRepository,
    randomEmojisProvider: RandomEmojisProvider,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _constructorLevelResponse = MutableLiveData<Result<List<EmojiShopModel?>>>()
    val constructorLevelResponse: LiveData<Result<List<EmojiShopModel?>>>
        get() = _constructorLevelResponse

    val hasTitleResponse : LiveData<Boolean> = levelsRepository.hasTitle

    lateinit var levelTitle: String
    val emojis by lazy {
        levelsRepository.getEmojis(levelTitle)
    }

    val randomEmojis = randomEmojisProvider.randomEmojis
    val userLevels = levelsRepository.levels

    init {
        fetchConstructor()
    }

    private fun fetchConstructor() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _constructorLevelResponse.value = Result.Loading
            }
            val result = levelConstructorService.fetchFakeData()
            withContext(Dispatchers.Main) {
                _constructorLevelResponse.value = result
            }
        }
    }

    fun saveLevel(level: List<EmojiShopModel>, smallLevelModel: SmallLevelModel) {
        coroutineScope.launch {
            levelsRepository.removeLevel(smallLevelModel.title)
            levelsRepository.addLevel(level, smallLevelModel)
        }
    }

    fun checkOnSameTitle(title : String){
        coroutineScope.launch {
            levelsRepository.checkOnSameTitle(title)
        }
    }

    fun sentLevel(
        level: List<EmojiShopModel>,
        smallLevelModel: SmallLevelModel?,
        image: Bitmap?
    ) {
        coroutineScope.launch {
            levelsRepository.sentLevel(level, smallLevelModel, image)
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun hasDifferences(
        currentList: List<EmojiShopModel>,
        smallLevelModel: SmallLevelModel
    ) {
        coroutineScope.launch {
            //levelsRepository.hasDifferences(currentList, smallLevelModel)
        }
    }

    fun deleteLevel(title: String) {
        coroutineScope.launch {
            levelsRepository.removeLevel(title)
        }
    }

    fun hasTitleComplete() {
        levelsRepository.hasTitleComplete()
    }

}