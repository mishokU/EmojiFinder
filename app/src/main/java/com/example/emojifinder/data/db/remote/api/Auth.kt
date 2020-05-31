package com.example.emojifinder.data.db.remote.api

import com.example.emojifinder.domain.auth.LoginModel
import com.example.emojifinder.domain.result.Result
import com.google.firebase.auth.AuthResult

interface Auth {
    fun isLogged() : Boolean
    suspend fun logIn(data : LoginModel) : Result<AuthResult>
    suspend fun logOut() : Result<Unit>
    suspend fun restorePassword(email : String) : Result<Void>
    suspend fun deleteAccount()
}