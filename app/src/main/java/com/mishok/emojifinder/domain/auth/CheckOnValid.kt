package com.mishok.emojifinder.domain.auth

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.mishok.emojifinder.R

object CheckOnValid {

    fun isEmailValid(email: TextInputEditText) : Boolean {
        return if(Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            true
        } else {
            email.error = email.resources.getString(R.string.email_error)
            false
        }
    }

    fun isPasswordsSimilar(
        password: TextInputEditText,
        repeatPassword: TextInputEditText
    ) : Boolean {
        return if(password.text.toString() == repeatPassword.text.toString()){
            true
        } else {
            password.error = password.resources.getString(R.string.passwords_error)
            false
        }
    }
}