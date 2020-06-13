package com.example.emojifinder.domain.user

import com.example.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.data.db.remote.service.FirebaseCollection
import com.example.emojifinder.data.db.remote.service.FirebaseInit
import com.example.emojifinder.ui.shop.EmojiShopModel
import javax.inject.Inject

interface FirebaseCreateUserAccount {
    suspend fun createMainInfoBrunch(login : String, email : String, password : String)
    fun createValuesBrunch()
    fun createLevelStatisticBrunch()
    fun createUserEmojisBrunch()
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
            mFireStore.collection("score")
                .document(mAuth.uid!!)
                .set(mainAccountInfo)
        }
    }

    override fun createValuesBrunch() {
        val values = AccountValuesModel(
            boxes = 0,
            emos = 200,
            emojis = 1
        )
        if(mAuth.uid != null){
            mFireStore
                .collection("users")
                .document(mAuth.uid!!)
                .collection("values")
                .document("data")
                .set(values)
        }
    }

    override fun createLevelStatisticBrunch() {
        TODO("Not yet implemented")
    }

    override fun createUserEmojisBrunch() {
        val emoji = EmojiShopModel(
            id = 0,
            codes = "1F600",
            text =  "\uD83D\uDE02",
            name = "grinning face",
            category = "Smileys & Emotion (face-smiling)",
            group = "Smileys & Emotion",
            subgroup = "face-smiling"
        )

        if(mAuth.uid != null){
            mFireStore
                .collection("users")
                .document(mAuth.uid!!)
                .collection("emojis")
                .document("😀")
                .set(emoji)
        }
    }
}