package com.example.emojifinder.domain.viewModels

import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeMain
import com.example.emojifinder.data.db.remote.api.FirebaseRegistration
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseUser : FirebaseRegistration,
    @CoroutineScopeMain
    val coroutineScope : CoroutineScope
) : ViewModel() {



}