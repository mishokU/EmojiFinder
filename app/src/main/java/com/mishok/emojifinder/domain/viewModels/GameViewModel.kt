package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.mishok.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import com.mishok.emojifinder.data.db.remote.service.FirebaseUserData
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.game.arcade.GameRecyclerViewAdapter
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import kotlinx.coroutines.*
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val levelStatistics: FirebaseLevelStatisticImpl,
    private val userMainData : FirebaseUserData,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _oneLevelEmojis = MutableLiveData<MutableList<EmojiShopModel?>>()
    val oneLevelEmojisData : LiveData<MutableList<EmojiShopModel?>>
        get() = _oneLevelEmojis

    private val _bottomEmojis = MutableLiveData<MutableList<EmojiShopModel?>>()
    val bottomEmojis : LiveData<MutableList<EmojiShopModel?>>
        get() = _bottomEmojis

    fun writeGameStatistic(title : String?, statistics: UserLevelStatistics){
        coroutineScope.launch {
            levelStatistics.writeLevelStatistic(title, statistics)
        }
    }

    fun updateScore(score : Int){
        coroutineScope.launch {
            userMainData.updateScore(score)
        }
    }

    fun updateEmos(emos : Int){
        coroutineScope.launch {
            when(val emosValues = userMainData.fetchUserValues()){
                is Result.Success -> {
                    userMainData.updateEmos((emosValues.data.emos + emos))
                }
            }
        }
    }

    fun countLevel(
        adapter: GameRecyclerViewAdapter,
        bottomEmojis: MutableList<EmojiShopModel?>,
        data: MutableList<EmojiShopModel?>,
        oneLevelEmojis: MutableList<EmojiShopModel?>
    ) {
        coroutineScope.launch {
            for(i : Int in 0..49){
                val emoji = data.random()
                oneLevelEmojis.add(emoji)
                bottomEmojis.add(emoji)
            }
            adapter.setData(oneLevelEmojis)
            withContext(Dispatchers.Main){
                _oneLevelEmojis.value = oneLevelEmojis
                _bottomEmojis.value = bottomEmojis
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}