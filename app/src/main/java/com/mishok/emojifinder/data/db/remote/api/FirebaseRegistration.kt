package com.mishok.emojifinder.data.db.remote.api

import com.google.firebase.auth.AuthResult
import com.mishok.emojifinder.domain.auth.RegistrationModel
import com.mishok.emojifinder.domain.result.Result

interface FirebaseRegistration{
    suspend fun register(data : RegistrationModel) : Result<AuthResult>
}