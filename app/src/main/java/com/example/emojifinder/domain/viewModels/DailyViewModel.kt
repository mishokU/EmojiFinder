package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.data.db.local.fake.FakeDailyItems
import com.example.emojifinder.data.db.local.models.DailyModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.emojifinder.domain.result.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DailyViewModel @Inject constructor(
    val localDaile : FakeDailyItems,
    @CoroutineScopeIO
    val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _dailies = MutableLiveData<Result<List<DailyModel>>>()
    val dailies : LiveData<Result<List<DailyModel>>>
        get() = _dailies

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