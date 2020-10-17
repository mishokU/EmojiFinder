package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.remote.api.FirebaseLevels
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.*
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val firebaseCategories: FirebaseLevels,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Result<List<SmallLevelModel?>>>()
    val categoriesResponse: LiveData<Result<List<SmallLevelModel?>>>
        get() = _categoriesResponse

    private val _levelResponse = MutableLiveData<Result<List<EmojiShopModel>>>()
    val levelResponse: LiveData<Result<List<EmojiShopModel>>>
        get() = _levelResponse

    private val _gameCategory = MutableLiveData<SmallLevelModel>()
    val gameCategory: LiveData<SmallLevelModel>
        get() = _gameCategory

    init {
        fetchLevels()
    }

    private fun fetchLevels() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _categoriesResponse.value = Result.Loading
            }
            val categories = firebaseCategories.fetchLevels()
            withContext(Dispatchers.Main) {
                _categoriesResponse.value = categories
            }
        }
    }

    fun fetchLevel(title: String?) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _levelResponse.value = Result.Loading
            }
            val level = firebaseCategories.fetchLevel(title)
            withContext(Dispatchers.Main) {
                _levelResponse.value = level
            }
        }
    }

    fun showGameFragment(category: SmallLevelModel?) {
        _gameCategory.value = category
    }

    fun gameFragmentComplete() {
        _gameCategory.value = null
        _levelResponse.value = null
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}