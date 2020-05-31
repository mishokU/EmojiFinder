package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.local.fake.LevelConstructorService
import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import kotlinx.coroutines.CoroutineScope
import com.example.emojifinder.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConstructorViewModel @Inject constructor(
    private val levelConstructorService: LevelConstructorService,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _constructorLevelResponse = MutableLiveData<Result<List<EmojiShopModel?>>>()
    val constructorLevelResponse : LiveData<Result<List<EmojiShopModel?>>>
        get() = _constructorLevelResponse

    init {
        coroutineScope.launch {
            fetchConstructor()
        }
    }

    private suspend fun fetchConstructor() {
        withContext(Dispatchers.Main){
            _constructorLevelResponse.value = Result.Loading
        }
        val result = levelConstructorService.fetchFakeData()
        withContext(Dispatchers.Main){
            _constructorLevelResponse.value = result
        }
    }
}