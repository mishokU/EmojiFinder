package com.mishok.emojifinder.data.db.remote.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.storage.FirebaseStorage
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
        var mUser : FirebaseUser? = mAuth.currentUser
        val mFireStore = FirebaseFirestore.getInstance()
        val mFireStorage = FirebaseStorage.getInstance().reference
        //val mNaturalLanguage = FirebaseNaturalLanguage.getInstance().getTranslator(options)
    }

}