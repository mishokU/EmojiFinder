package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeIO
import com.example.emojifinder.core.di.utils.CoroutineScopeMain
import com.example.emojifinder.data.db.remote.api.FirebaseCategories
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.CategoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    firebaseCategories: FirebaseCategories,
    @CoroutineScopeIO
    val coroutineScope : CoroutineScope
) : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Result<List<CategoryModel?>>>()
    val categoriesResponse : LiveData<Result<List<CategoryModel?>>>
        get() = _categoriesResponse

    private val _gameCategory = MutableLiveData<CategoryModel>()
    val gameCategory : LiveData<CategoryModel>
        get() = _gameCategory


    init {
        coroutineScope.launch {
            _categoriesResponse.value = Result.Loading
            val categories = firebaseCategories.fetchCategories()

            withContext(Dispatchers.Main){
                _categoriesResponse.value = categories
            }
        }
    }

    fun showGameFragment(category: CategoryModel?) {
        _gameCategory.value = category
    }

    fun gameFragmentComplete(){
        _gameCategory.value = null
    }

}