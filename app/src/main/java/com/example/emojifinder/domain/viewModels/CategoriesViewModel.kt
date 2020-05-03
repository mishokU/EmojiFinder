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
    val firebaseCategories: FirebaseCategories,
    @CoroutineScopeIO
    val coroutineScope : CoroutineScope
) : ViewModel() {

    private val _categoriesResponse = MutableLiveData<Result<List<CategoryModel?>>>()
    val categoriesResponse : LiveData<Result<List<CategoryModel?>>>
        get() = _categoriesResponse

    private val _levelResponse = MutableLiveData<Result<List<HashMap<String, Any?>>>>()
    val levelResponse : LiveData<Result<List<HashMap<String, Any?>>>>
        get() = _levelResponse

    private val _gameCategory = MutableLiveData<CategoryModel>()
    val gameCategory : LiveData<CategoryModel>
        get() = _gameCategory


    init {
        coroutineScope.launch {
            withContext(Dispatchers.Main){
                _categoriesResponse.value = Result.Loading
            }
            val categories = firebaseCategories.fetchCategories()

            withContext(Dispatchers.Main){
                _categoriesResponse.value = categories
            }
        }
    }

    fun fetchLevel(title : String){
        coroutineScope.launch {
           _levelResponse.value = Result.Loading
            val level = firebaseCategories.fetchLevel(title)

            withContext(Dispatchers.Main){
                _levelResponse.value = level
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