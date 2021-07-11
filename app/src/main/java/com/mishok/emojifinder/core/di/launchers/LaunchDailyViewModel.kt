package com.mishok.emojifinder.core.di.launchers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mishok.emojifinder.core.di.utils.LaunchFragmentDestination
import com.mishok.emojifinder.domain.result.Event
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.domain.usecase.MainMenuDailyCompletedUseCase
import com.mishok.emojifinder.shared.utils.map
import javax.inject.Inject

class LaunchDailyViewModel @Inject constructor(
    mainMenuDailyCompletedUseCase: MainMenuDailyCompletedUseCase
) : ViewModel() {

    private val signInCompletedResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchFragmentDestination>>

    init {
        mainMenuDailyCompletedUseCase(Unit, signInCompletedResult)
        launchDestination = signInCompletedResult.map {
            if ((it as? Result.Success)?.data == false) {
                Event(LaunchFragmentDestination.MAIN_MENU_FRAGMENT)
            } else {
                Event(LaunchFragmentDestination.DAILY_FRAGMENT)
            }
        }
    }
}