package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel
import javax.inject.Inject

class ShopViewModel @Inject constructor() : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Result<List<SmallLevelModel?>>>()
    val categoriesResponse : LiveData<Result<List<SmallLevelModel?>>>
        get() = _categoriesResponse
}