package com.mishok.emojifinder.data.db.remote.service

import android.graphics.Bitmap
import com.mishok.emojifinder.data.db.remote.api.FirebaseLevels
import com.mishok.emojifinder.data.db.remote.models.EmojiShopModel
import com.mishok.emojifinder.data.db.remote.models.ListEmojiShopModel
import com.mishok.emojifinder.domain.result.Result
import com.mishok.emojifinder.ui.categories.SmallLevelModel
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class FirebaseLevelsImpl : FirebaseInit(), FirebaseLevels {

    override suspend fun fetchLevel(title: String?) : Result<List<EmojiShopModel>>{
        return try {
            val documents = mFireStore
                .collection("levels")
                .document(title!!)
                .collection("emojis")
                .document("level")
                .get().await()

            val levelSnapshot : List<EmojiShopModel> = documents.toObject(ListEmojiShopModel::class.java)!!.level
            for(level in levelSnapshot){
                //mNaturalLanguage.translate(level!!.title)
            }

            Result.Success(levelSnapshot)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    override suspend fun addFullLevel(
        level: List<EmojiShopModel>,
        smallLevelModel: SmallLevelModel?,
        image: Bitmap?
    ) {

        val map : HashMap<String, List<EmojiShopModel>> = HashMap()
        map["level"] = level.filter {
            it.unicode != ""
        }

        mFireStore
            .collection("userLevels")
            .document(smallLevelModel?.title!!)
            .set(smallLevelModel)

        mFireStore.collection("userLevels")
            .document(smallLevelModel.title)
            .collection("emojis")
            .document("level")
            .set(map)

        putBitmap(smallLevelModel,image)
    }

    override suspend fun hasThisTitle(title: String): Boolean {
        try {
            val document = mFireStore
                .collection("levels")
                .orderBy("id")
                .get()
                .await()

            for(data in document.documents){
                val level = data.toObject(SmallLevelModel::class.java)!!
                if(level.title == title){
                    return true
                }
            }
        } catch (e : Exception){ }
        return false
    }

    override suspend fun hasThisTitleInUserLevels(title: String): Boolean {
        try {
            val document = mFireStore
                .collection("userLevels")
                .orderBy("id")
                .get()
                .await()

            for(data in document.documents){
                val level = data.toObject(SmallLevelModel::class.java)!!
                if(level.title == title){
                    return true
                }
            }
        } catch (e : Exception){ }
        return false
    }

    private fun putBitmap(
        path: SmallLevelModel,
        image: Bitmap?
    ) {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        mFireStorage.child("image/${path.title}").putBytes(data)
    }

    override suspend fun fetchLevels() : Result<List<SmallLevelModel>>{
        return try {
            val document = mFireStore
                .collection("levels")
                .orderBy("id")
                .get()
                .await()

            val list : MutableList<SmallLevelModel> = mutableListOf()
            for(data in document.documents){
                val level = data.toObject(SmallLevelModel::class.java)!!
                //mNaturalLanguage.translate(level.title)
                list.add(level)
            }

            Result.Success(list.toList())
        } catch (e : Exception){
            Result.Error(e)
        }
    }
}