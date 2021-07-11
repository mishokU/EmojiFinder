package com.mishok.emojifinder.data.db.remote.service

import com.mishok.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.mishok.emojifinder.domain.result.Result
import kotlinx.coroutines.tasks.await

class FirebaseLevelStatisticImpl : FirebaseInit() {

    fun writeLevelStatistic(title : String?, statistics: UserLevelStatistics){
        if(mUser == null){
            mUser = mAuth.currentUser
        }
        mFireStore
            .collection("users")
            .document(mUser!!.uid)
            .collection("levels")
            .document(title!!)
            .set(statistics)
    }

    suspend fun fetchUserLevelsStatistic(): Result<List<UserLevelStatistics?>>{
        return try {
            if(mUser == null){
                mUser = mAuth.currentUser
            }

            val document =  mFireStore
                .collection("users")
                .document(mUser!!.uid)
                .collection("levels")
                .orderBy("id")
                .get()
                .await()

            val levels : MutableList<UserLevelStatistics?> = mutableListOf()
            for(level in document.documents){
                levels.add(level.toObject(UserLevelStatistics::class.java))
            }

            Result.Success(levels)
        } catch (e : Exception){
            Result.Error(e)
        }
    }
}