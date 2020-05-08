package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.api.FirebaseLevels
import com.example.emojifinder.data.db.remote.models.EmojiModel
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseLevelsImpl : FirebaseInit(), FirebaseLevels {

    override suspend fun fetchLevel(title : String?) : Result<List<EmojiModel?>>{
        return try {
            val documents = mFireStore
                .collection("categories")
                .document(title!!)
                .collection("emojis")
                .get().await()

            println(documents.documents[0].data)

            val level : MutableList<EmojiModel?> = mutableListOf()
            for(document in documents.documents){
                level.add(document.toObject(EmojiModel::class.java))
            }
            Result.Success(level)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    override suspend fun fetchLevels() : Result<List<SmallLevelModel?>>{
        return try {
            val document = mFireStore
                .collection("categories")
                .get()
                .await()

            val list : MutableList<SmallLevelModel?> = mutableListOf()
            for(data in document.documents){
                list.add(data.toObject(SmallLevelModel::class.java))
            }

            Result.Success(list.toList())
        } catch (e : Exception){
            Result.Error(e)
        }
    }
}