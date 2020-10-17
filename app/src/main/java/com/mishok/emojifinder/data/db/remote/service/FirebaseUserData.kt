package com.mishok.emojifinder.data.db.remote.service

import com.mishok.emojifinder.data.db.remote.models.account.AccountValuesModel
import com.mishok.emojifinder.data.db.remote.models.account.MainAccountModel
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.shop.EmojiShopModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.tasks.await


class FirebaseUserData : FirebaseInit() {

    suspend fun fetchUserMainInfo() : Result<MainAccountModel> {
        return try {
            val info = mFireStore.collection("score")
                .document(mAuth.uid!!)
                .get()
                .await()

            Result.Success(info.toObject(MainAccountModel::class.java)!!)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    suspend fun fetchUserEmojis() : Result<List<EmojiShopModel>> {
        return try {
            val emojisQuery = mFireStore
                .collection("users")
                .document(mAuth.uid!!)
                .collection("emojis")
                .get()
                .await()

            val emojisList : MutableList<EmojiShopModel> = mutableListOf()
            for(emoji in emojisQuery){
                emojisList.add(emoji.toObject(EmojiShopModel::class.java))
            }
            Result.Success(emojisList)

        } catch (e: Exception){
            Result.Error(e)
        }
    }

    fun updateLogin(login : String) {
        mFireStore
            .collection("score")
            .document(mAuth.uid!!)
            .update("login", login)
    }

    suspend fun updateEmailAndPassword(
        old_email : String, old_password : String,
        new_email : String, new_password : String) : Result<Void?> = try {

        val data : HashMap<String, String> = hashMapOf()
        data["email"] = new_email
        data["password"] = new_password

        mFireStore
            .collection("score")
            .document(mAuth.uid!!)
            .update(data as Map<String, Any>)

        Result.Success(reauthenticate(
            old_email, old_password ,
            new_email, new_password))
    } catch (e : Exception){
        Result.Error(e)
    }

    private suspend fun reauthenticate(
        email: String,
        password: String,
        newEmail: String,
        newPassword: String
    ) : Void? {
        val credential = EmailAuthProvider.getCredential(email, password)
        val result =  mUser?.reauthenticate(credential)
            ?.addOnCompleteListener(OnCompleteListener {
                //Now change your email address \\
                //----------------Code for Changing Email Address----------\\
                mAuth.currentUser!!.updateEmail(newEmail)
                mAuth.currentUser!!.updatePassword(newPassword)
            })
        return result?.await()
    }

    fun updateScore(score: Int) {
        mFireStore
            .collection("score")
            .document(mUser!!.uid)
            .update("score", score)
    }

    suspend fun fetchUserValues() : Result<AccountValuesModel> {
        return try {
            val valuesQuery = mFireStore
                .collection("users")
                .document(mAuth.uid!!)
                .collection("values")
                .get()
                .await()

            val values : MutableList<AccountValuesModel> = mutableListOf()
            for(emoji in valuesQuery){
                values.add(emoji.toObject(AccountValuesModel::class.java))
            }

            Result.Success(values[0])
        } catch(e : Exception){
            Result.Error(e)
        }
    }

    fun addEmoji(emoji: EmojiShopModel?) {
        mFireStore.collection("users")
            .document(mAuth.uid!!)
            .collection("emojis")
            .document(emoji!!.text)
            .set(emoji)
    }

    fun removeEmoji(emoji: EmojiShopModel) {
        mFireStore.collection("users")
            .document(mAuth.uid!!)
            .collection("emojis")
            .document(emoji.text)
            .delete()
    }

    fun updateValues(cost: Int, values : AccountValuesModel) {

        if(cost != 0){
            if(cost > 0){
                values.emojis -= 1
                values.emos += cost
            } else {
                values.emojis += 1
                values.emos += cost
            }
        } else {
            values.emojis += 1
            println(values.emojis)
        }

        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("values")
            .document("data")
            .set(values)
    }

    fun updateAvatar(avatar: String) {
        mFireStore
            .collection("score")
            .document(mUser!!.uid)
            .update("avatar", avatar)
    }

    fun updateEmos(emos: Int) {
        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("values")
            .document("data")
            .update("emos", emos)
    }

    fun updateBoxes(boxes: Int) {
        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("values")
            .document("data")
            .update("boxes", boxes)

    }

    fun updateEmojis(emojisCount: Int) {
        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("values")
            .document("data")
            .update("emojis", emojisCount)
    }
}