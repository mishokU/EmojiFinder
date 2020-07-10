package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.domain.glide.MyAppGlideModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

open class FirebaseInit  {

    fun deviceLanguage(): Int {
        println(Locale.getDefault().language)
        return when(Locale.getDefault().language){
            "en" -> FirebaseTranslateLanguage.EN
            "ru" -> FirebaseTranslateLanguage.RU
            else -> FirebaseTranslateLanguage.EN
        }
    }

//    suspend fun downloadLanguage() {
//        mNaturalLanguage.downloadModelIfNeeded()
//            .addOnSuccessListener {
//                println("success")
//            }.addOnFailureListener {
//                println(it.message)
//        }.await()
//    }

    companion object {
        val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
        val mUser : FirebaseUser? = mAuth.currentUser
        val mFireStore = FirebaseFirestore.getInstance()
        val mFireStorage = FirebaseStorage.getInstance().reference
        //val mNaturalLanguage = FirebaseNaturalLanguage.getInstance().getTranslator(options)
    }

}