package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.local.fake.FakeDailyItems
import com.mishok.emojifinder.data.db.local.models.DailyModel
import com.mishok.emojifinder.domain.prefs.DailyWinningsPrefs
import com.mishok.emojifinder.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DailyViewModel @Inject constructor(
    val localDaile : FakeDailyItems,
    val dailyWinningsPrefs: DailyWinningsPrefs,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _dailies = MutableLiveData<Result<List<DailyModel>>>()
    val dailies : LiveData<Result<List<DailyModel>>>
        get() = _dailies

    private val _isNextDay = MutableLiveData<Boolean>()
    val isNextDay : LiveData<Boolean>
        get() = _isNextDay

    init {
        fetchDailies()
    }

    private fun fetchDailies() {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _dailies.value = Result.Loading
            }
            val result  = localDaile.fetchFakeData()
            withContext(Dispatchers.Main){
                _dailies.value = result
            }
        }
    }

}