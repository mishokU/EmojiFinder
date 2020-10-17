package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.mishok.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import com.mishok.emojifinder.data.db.remote.service.FirebaseUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.mishok.emojifinder.domain.result.Result

import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val levelStatistics: FirebaseLevelStatisticImpl,
    private val userMainData : FirebaseUserData,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

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

}