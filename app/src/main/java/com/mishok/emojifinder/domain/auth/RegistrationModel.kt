package com.mishok.emojifinder.domain.auth

data class RegistrationModel(
    val email : String,
    val login : String,
    val password : String
)