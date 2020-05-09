package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.remote.api.FirebaseLevels
import com.example.emojifinder.data.db.remote.models.EmojiModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val firebaseCategories: FirebaseLevels,
    @CoroutineScopeIO
    val coroutineScope : CoroutineScope
) : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Result<List<SmallLevelModel?>>>()
    val categoriesResponse : LiveData<Result<List<SmallLevelModel?>>>
        get() = _categoriesResponse

    private val _levelResponse = MutableLiveData<Result<List<EmojiModel?>>>()
    val levelResponse : LiveData<Result<List<EmojiModel?>>>
        get() = _levelResponse

    private val _gameCategory = MutableLiveData<SmallLevelModel>()
    val gameCategory : LiveData<SmallLevelModel>
        get() = _gameCategory


    init {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _categoriesResponse.value = Result.Loading
            }
            val categories = firebaseCategories.fetchLevels()
            withContext(Dispatchers.Main){
                _categoriesResponse.value = categories
            }
        }
    }

    fun fetchLevel(title : String?){
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _levelResponse.value = Result.Loading
            }
            val level = firebaseCategories.fetchLevel(title)

            withContext(Dispatchers.Main){
                _levelResponse.value = level
            }
        }
    }

    fun showGameFragment(category: SmallLevelModel?) {
        _gameCategory.value = category
    }

    fun gameFragmentComplete(){
        _gameCategory.value = null
    }

}