package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.models.account.UserLevelStatistics
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.CategoryModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseLevelStatisticImpl : FirebaseInit() {

    suspend fun writeLevelStatistic(title : String?, statistics: UserLevelStatistics){

        val level : HashMap<String, UserLevelStatistics> = hashMapOf()
        level[title!!] = statistics

        mFireStore
            .collection("users")
            .document(mUser!!.uid)
            .collection("levels")
            .document(title)
            .set(level)
    }

    suspend fun fetchUserLevelStatistic(level : String?): Result<UserLevelStatistics?>{
        return try {
            val document =  mFireStore
                .collection("users")
                .document(mUser!!.uid)
                .collection("levels")
                .document(level!!)
                .get()
                .await()

            Result.Success(document.toObject(UserLevelStatistics::class.java))
        } catch (e : Exception){
            Result.Error(e)
        }
    }

}