package com.example.emojifinder.domain.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.emojifinder.R
import com.example.emojifinder.domain.presenters.NavigationPresenter
import javax.inject.Inject

class SignInViewModel @Inject constructor(): ViewModel() {

    fun toLogin(view : View){
        Navigation.findNavController(view).navigate(R.id.logInFragment)
    }

    fun toRegistration(view : View){
        Navigation.findNavController(view).navigate(R.id.registrationFragment)
    }


}