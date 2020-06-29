package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.api.Auth
import com.example.emojifinder.domain.auth.LoginModel
import com.example.emojifinder.domain.result.Result
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Singleton


@Singleton
class FirebaseAuthHandler : Auth, FirebaseInit() {

    override fun isLogged() : Boolean = mUser != null

    override suspend fun logIn(data : LoginModel) : Result<AuthResult> {
        return try {
            val result = mAuth.signInWithEmailAndPassword(data.email, data.password).await()

            downloadLanguage()

            Result.Success(result)
        }catch (e : Exception){
            Result.Error(e)
        }
    }

    override suspend fun restorePassword(email: String): Result<Void> {
        return try {
            val result = mAuth.sendPasswordResetEmail(email).await()
            return Result.Success(result)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    override suspend fun logOut() : Result<Unit> {
        return Result.Success(mAuth.signOut())
    }

    override suspend fun deleteAccount() {
        mFireStore.collection("users")
            .document(mUser!!.uid)
            .delete()

        mAuth.signOut()
        mUser.delete()
    }

}