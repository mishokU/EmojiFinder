package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.ui.constructor.choosePhoto.PhotoPickerModel

class SharedViewModel : ViewModel() {

    val userLogin = MutableLiveData<String>()
    val languageFlag = MutableLiveData<String>()
    val photo = MutableLiveData<PhotoPickerModel>()

    fun updateLoginView(login : String){
        userLogin.value = login
    }

    fun updateLanguageFlag(language: String){
        languageFlag.value = language
    }

    fun sentImage(photo : PhotoPickerModel){
        this.photo.value = photo
    }

}