package com.example.emojifinder.domain.presenters

import android.view.View
import androidx.navigation.Navigation.findNavController
import com.example.emojifinder.ui.application.MainApplication
import javax.inject.Inject
import javax.inject.Singleton

class NavigationPresenter {

    fun navigate(view : View, id : Int){
        findNavController(view).navigate(id)
    }
}