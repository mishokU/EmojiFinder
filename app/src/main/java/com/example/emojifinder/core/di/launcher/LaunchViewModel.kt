/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.emojifinder.core.di.launcher


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.domain.prefs.SignInCompletedUseCase
import com.example.emojifinder.domain.result.Event
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.shared.utils.map
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
class LaunchViewModel @Inject constructor(
    signInCompletedUseCase: SignInCompletedUseCase
) : ViewModel() {

    private val signInCompletedResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

    init {
        signInCompletedUseCase(Unit, signInCompletedResult)
        launchDestination = signInCompletedResult.map {
            if ((it as? Result.Success)?.data == false) {
                Event(LaunchDestination.SIGN_IN_ACTIVITY)
            } else {
                Event(LaunchDestination.MAIN_ACTIVITY)
            }
        }
    }
}

enum class LaunchDestination {
    SIGN_IN_ACTIVITY,
    MAIN_ACTIVITY
}
