package com.mishok.emojifinder.domain.presenters

import android.view.View
import androidx.navigation.Navigation.findNavController

class NavigationPresenter {

    fun navigate(view : View, id : Int){
        findNavController(view).navigate(id)
    }
}