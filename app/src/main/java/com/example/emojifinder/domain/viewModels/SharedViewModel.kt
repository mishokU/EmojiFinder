package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SharedViewModel : ViewModel() {

    val userLogin = MutableLiveData<String>()

    fun updateLoginView(login : String){
        userLogin.value = login
    }

}