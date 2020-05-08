package com.example.emojifinder.domain.viewModels

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.core.di.utils.CoroutineScopeMain
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val levelStatistics: FirebaseLevelStatisticImpl,
    @CoroutineScopeMain
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _statisticResponse = MutableLiveData<Result<UserLevelStatistics?>>()
    val statisticResponse : LiveData<Result<UserLevelStatistics?>>
        get() = _statisticResponse

    fun writeGameStatistic(title : String?, statistics: UserLevelStatistics){
        coroutineScope.launch {
            levelStatistics.writeLevelStatistic(title, statistics)
        }
    }

    fun statisticResponseComplete() {
        _statisticResponse.value = null
    }
}