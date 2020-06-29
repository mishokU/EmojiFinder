package com.example.emojifinder.data.db.remote.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.*

open class FirebaseInit  {

    private val options = FirebaseTranslatorOptions.Builder()
        .setSourceLanguage(FirebaseTranslateLanguage.EN)
        .setTargetLanguage(deviceLanguage())
        .build()

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mUser : FirebaseUser? = mAuth.currentUser
    val mFireStore = FirebaseFirestore.getInstance()
    val mNaturalLanguage = FirebaseNaturalLanguage.getInstance().getTranslator(options)

    private fun deviceLanguage(): Int {
        println(Locale.getDefault().language)
        return when(Locale.getDefault().language){
            "en" -> FirebaseTranslateLanguage.EN
            "ru" -> FirebaseTranslateLanguage.RU
            else -> FirebaseTranslateLanguage.EN
        }
    }

    suspend fun downloadLanguage() {
        mNaturalLanguage.downloadModelIfNeeded()
            .addOnSuccessListener {
                println("success")
            }.addOnFailureListener {
                println(it.message)
        }.await()
    }
}