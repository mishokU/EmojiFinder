package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.data.db.remote.service.FirebaseLevelStatisticImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val levelStatistics: FirebaseLevelStatisticImpl,

    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    fun writeGameStatistic(title : String?, statistics: UserLevelStatistics){
        coroutineScope.launch {
            levelStatistics.writeLevelStatistic(title, statistics)
        }
    }

}