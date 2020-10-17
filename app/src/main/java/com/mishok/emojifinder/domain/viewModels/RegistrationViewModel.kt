package com.mishok.emojifinder.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.mishok.emojifinder.core.di.utils.CoroutineScopeMain
import com.mishok.emojifinder.data.db.remote.api.FirebaseRegistration
import com.mishok.emojifinder.domain.auth.CheckOnValid
import com.mishok.emojifinder.domain.auth.RegistrationModel
import com.mishok.emojifinder.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val firebaseRegistration : FirebaseRegistration,
    @CoroutineScopeMain
    val coroutineScope : CoroutineScope
) : ViewModel() {

    private val _registrationResponse = MutableLiveData<Result<AuthResult>>()
    val registrationResponse : LiveData<Result<AuthResult>>
        get() = _registrationResponse


    fun registration(
        login: TextInputEditText,
        email: TextInputEditText,
        password: TextInputEditText,
        repeatPassword: TextInputEditText
    ){
        coroutineScope.launch {
            if(
                CheckOnValid.isEmailValid(email) &&
                CheckOnValid.isPasswordsSimilar(password,repeatPassword)
            ) {

                _registrationResponse.value = Result.Loading

                val data = RegistrationModel(
                    login =login.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString()
                )

                _registrationResponse.value = firebaseRegistration.register(data)
            }
        }
    }
}