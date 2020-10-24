package com.mishok.emojifinder.domain.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.CoroutineScopeIO
import com.mishok.emojifinder.data.db.local.photos.PhotosDataSource
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.constructor.choosePhoto.PhotoPickerModel
import kotlinx.coroutines.*
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
    private val application: Application,
    @CoroutineScopeIO
    private val coroutine : CoroutineScope
) : ViewModel() {

    @Inject
    lateinit var photosDataSource: PhotosDataSource

    private val _images = MutableLiveData<Result<List<PhotoPickerModel>>>()
    val images : LiveData<Result<List<PhotoPickerModel>>>
        get() = _images

    init {
        coroutine.launch {
            loadImages()
        }
    }

    private suspend fun loadImages() {
        withContext(Dispatchers.Main){
            _images.value = Result.Loading
        }
        val images = photosDataSource.onLoadFinished(application.baseContext)
        withContext(Dispatchers.Main){
            _images.value = images
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutine.cancel()
    }
}