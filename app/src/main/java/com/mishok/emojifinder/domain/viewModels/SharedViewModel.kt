package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    val userLogin = MutableLiveData<String>()

    fun updateLoginView(login : String){
        userLogin.value = login
    }

}