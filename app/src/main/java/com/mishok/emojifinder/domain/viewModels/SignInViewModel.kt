package com.mishok.emojifinder.domain.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.mishok.emojifinder.R
import javax.inject.Inject

class SignInViewModel @Inject constructor(

): ViewModel() {

    fun toLogin(view : View){
        Navigation.findNavController(view).navigate(R.id.logInFragment)
    }

    fun toRegistration(view : View){
        Navigation.findNavController(view).navigate(R.id.registrationFragment)
    }


}