package com.example.emojifinder.domain.viewModels

import com.example.emojifinder.domain.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emojifinder.core.di.utils.CoroutineScopeMain
import com.example.emojifinder.domain.auth.CheckOnValid
import com.example.emojifinder.data.db.remote.service.FirebaseAuthHandler
import com.example.emojifinder.domain.auth.LoginModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


class LogInViewModel @Inject constructor(
    private val firebaseAuthHandler: FirebaseAuthHandler,
    @CoroutineScopeMain
    val coroutineScope : CoroutineScope
) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<AuthResult>>()
    val loginResponse : LiveData<Result<AuthResult>>
        get() = _loginResponse

    private val _restorePasswordResponse = MutableLiveData<Result<Void>>()
    val restorePasswordResponse : LiveData<Result<Void>>
        get() = _restorePasswordResponse

    fun login(
        email: TextInputEditText,
        password: TextInputEditText
    ) {
        coroutineScope.launch {
            if (CheckOnValid.isEmailValid(email)) {

                _loginResponse.value = Result.Loading

                val loginModel = LoginModel(
                    email = email.text.toString(),
                    password = password.text.toString()
                )

                _loginResponse.value = firebaseAuthHandler.logIn(loginModel)
            }
        }
    }

    fun forgetPassword(email: TextInputEditText) {
        coroutineScope.launch {
            if(CheckOnValid.isEmailValid(email)){
                _restorePasswordResponse.value = Result.Loading
                _restorePasswordResponse.value = firebaseAuthHandler.restorePassword(email.text.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    fun logOut() {
        coroutineScope.launch {
            firebaseAuthHandler.logOut()
        }
    }

    fun deleteAccount() {

    }
}