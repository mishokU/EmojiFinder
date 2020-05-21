package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.local.emoji_json.ShopEmojiService
import com.example.emojifinder.data.db.local.emoji_json.getJsonDataFromAsset
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.android.support.DaggerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopViewModel @Inject constructor(
    private val shopEmojiService: ShopEmojiService,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _emojisResponse = MutableLiveData<Result<List<EmojiShopModel?>>>()
    val emojisResponse : LiveData<Result<List<EmojiShopModel?>>>
        get() = _emojisResponse

    init {
        coroutineScope.launch {
            loadEmojisFromJson()
        }
    }

    fun fetchShopEmojis(){

    }

    private suspend fun loadEmojisFromJson() {
        withContext(Dispatchers.Main){
            _emojisResponse.value = Result.Loading
        }
        val emojis = shopEmojiService.fetchEmojis()

        withContext(Dispatchers.Main){
            _emojisResponse.value = emojis
        }
    }
}