package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.domain.result.Result
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseLevelStatisticImpl : FirebaseInit() {

    fun writeLevelStatistic(title : String?, statistics: UserLevelStatistics){

        mFireStore
            .collection("users")
            .document(mUser!!.uid)
            .collection("levels")
            .document(title!!)
            .set(statistics)
    }

    suspend fun fetchUserLevelsStatistic(): Result<List<UserLevelStatistics?>>{
        return try {
            val document =  mFireStore
                .collection("users")
                .document(mUser!!.uid)
                .collection("levels")
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