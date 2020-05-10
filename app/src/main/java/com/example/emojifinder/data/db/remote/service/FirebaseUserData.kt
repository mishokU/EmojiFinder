package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.domain.result.Result
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.tasks.await


class FirebaseUserData : FirebaseInit() {

    suspend fun fetchUserMainInfo() : Result<MainAccountModel> {
        return try {
            val info = mFireStore.collection("users")
                .document(mAuth.uid!!)
                .collection("main")
                .get()
                .await()

            val main : MutableList<MainAccountModel> = mutableListOf()
            for(doc in info){
                main.add(doc.toObject(MainAccountModel::class.java))
            }

            Result.Success(main[0])
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    fun updateLogin(login : String) {
        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("main")
            .document("data")
            .update("login", login)
    }

    suspend fun updateEmailAndPassword(
        old_email : String, old_password : String,
        new_email : String, new_password : String) : Result<Void?> = try {

        val data : HashMap<String, String> = hashMapOf()
        data["email"] = new_email
        data["password"] = new_password

        mFireStore
            .collection("users")
            .document(mAuth.uid!!)
            .collection("main")
            .document("data")
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
            .collection("users")
            .document(mUser!!.uid)
            .collection("main")
            .document("data")
            .update("score", score)
    }
}