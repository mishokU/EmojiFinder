package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.local.emoji_json.ShopEmojiService
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.shop.EmojiShopModel
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

    private val _emojisResponse = MutableLiveData<Result<List<EmojiShopModel>>>()
    val emojisResponse: LiveData<Result<List<EmojiShopModel>>>
        get() = _emojisResponse

    private val _emojisDailyResponse = MutableLiveData<Result<List<EmojiShopModel?>>>()
    val emojisDailyResponse: LiveData<Result<List<EmojiShopModel?>>>
        get() = _emojisDailyResponse

    init {
        loadEmojisFromJson()
    }

    private fun loadEmojisFromJson() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _emojisResponse.value = Result.Loading
            }
            val emojis = shopEmojiService.fetchEmojis()
            withContext(Dispatchers.Main) {
                _emojisResponse.value = emojis
            }
        }
    }

    fun loadDailyEmojisFromJson() {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                _emojisDailyResponse.value = Result.Loading
            }
            val emojis = shopEmojiService.fetchDailyEmojis()
            withContext(Dispatchers.Main) {
                _emojisDailyResponse.value = emojis
            }
        }
    }

}