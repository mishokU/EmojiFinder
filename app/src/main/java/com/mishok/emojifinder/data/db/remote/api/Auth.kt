package com.mishok.emojifinder.data.db.remote.api

import com.google.firebase.auth.AuthResult
import com.mishok.emojifinder.domain.auth.LoginModel
import com.mishok.emojifinder.domain.result.Result

interface Auth {
    fun isLogged() : Boolean
    suspend fun logIn(data : LoginModel) : Result<AuthResult>
    suspend fun logOut() : Result<Unit>
    suspend fun restorePassword(email : String) : Result<Void>
    suspend fun deleteAccount()
}