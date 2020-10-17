package com.mishok.emojifinder.data.db.remote.service

import com.mishok.emojifinder.data.db.remote.api.FirebaseRegistration
import com.mishok.emojifinder.domain.auth.RegistrationModel
import com.mishok.emojifinder.domain.user.FirebaseCreateUserAccount
import com.google.firebase.auth.AuthResult
import com.mishok.emojifinder.domain.result.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class FirebaseRegistrationImpl @Inject constructor(
    private val createUserAccount: FirebaseCreateUserAccount)
    : FirebaseRegistration, FirebaseInit() {

    override suspend fun register(data: RegistrationModel) : Result<AuthResult> {
        return try{
            val result = mAuth
                .createUserWithEmailAndPassword(data.email,data.password)
                .await()
            if(result != null){
                createUserAccount.createMainInfoBrunch(data.login, data.email, data.password)
                createUserAccount.createValuesBrunch()
                createUserAccount.createUserEmojisBrunch()
            }
            Result.Success(result)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

}