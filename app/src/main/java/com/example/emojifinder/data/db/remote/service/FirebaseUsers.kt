package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.models.account.MainAccountModel
import com.example.emojifinder.domain.result.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class FirebaseUsers : FirebaseInit() {

    suspend fun fetchUsers() : Result<List<MainAccountModel>> {
        return try{

            val users = mFireStore
                .collection("score")
                .limit(50)
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .await()

            val listOfUsers : MutableList<MainAccountModel> = mutableListOf()
            for(user in users.documents){
                listOfUsers.add(user.toObject(MainAccountModel::class.java)!!)
            }
            Result.Success(listOfUsers)
        } catch (e : Exception){
            Result.Error(e)
        }
    }
}