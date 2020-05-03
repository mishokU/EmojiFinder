package com.example.emojifinder.data.db.remote.api

import com.example.emojifinder.domain.auth.RegistrationModel
import com.example.emojifinder.domain.result.Result
import com.google.firebase.auth.AuthResult

interface FirebaseRegistration{
    suspend fun register(data : RegistrationModel) : Result<AuthResult>
}