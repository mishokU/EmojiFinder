package com.example.emojifinder.domain.user

import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.data.db.remote.service.FirebaseCollection
import com.example.emojifinder.data.db.remote.service.FirebaseInit
import javax.inject.Inject

interface FirebaseCreateUserAccount {
    suspend fun createMainInfoBrunch(login : String, email : String, password : String)
    fun createValuesBrunch()
    fun createLevelStatisticBrunch()
}

class FirebaseCreateUserAccountImpl @Inject constructor(private val collection: FirebaseCollection)
    : FirebaseCreateUserAccount, FirebaseInit() {

    override suspend fun createMainInfoBrunch(login : String, email : String, password : String) {
        val mainAccountInfo = MainAccountModel(
            login = login,
            email = email,
            password = password,
            score = 0,
            avatar = "\uD83D\uDE02"
        )
        if(mAuth.uid != null){
            mFireStore.collection("users")
                .document(mAuth.uid!!)
                .collection("main")
                .document("data")
                .set(mainAccountInfo)
        }
    }

    override fun createValuesBrunch() {
        TODO("Not yet implemented")
    }

    override fun createLevelStatisticBrunch() {
        TODO("Not yet implemented")
    }
}