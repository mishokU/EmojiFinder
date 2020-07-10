package com.example.emojifinder.data.db.remote.service

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import java.util.*

class FirebaseTranslator : FirebaseInit() {

    init {
        initTranslator()
    }

    private fun initTranslator() {
    }

    private val options = FirebaseTranslatorOptions.Builder()
        .setSourceLanguage(FirebaseTranslateLanguage.EN)
        .setTargetLanguage(deviceLanguage())
        .build()

//    private fun deviceLanguage(): Int {
//        println(Locale.getDefault().language)
//        return when(Locale.getDefault().language){
//            "en" -> FirebaseTranslateLanguage.EN
//            "ru" -> FirebaseTranslateLanguage.RU
//            else -> FirebaseTranslateLanguage.EN
//        }
//    }


}