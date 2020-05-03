package com.example.emojifinder.data.db.remote.service

import com.example.emojifinder.data.db.remote.api.FirebaseCategories
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.CategoryModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseLevelsImpl : FirebaseInit(), FirebaseCategories {

    override suspend fun fetchLevel(title : String) : Result<List<HashMap<String, Any?>>>{
        return try {
            val document = mFireStore
                .collection("categories")
                .document(title)
                .get().await()

            print(document)

            val list : List<HashMap<String, Any?>> = listOf()

            Result.Success(list)
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    override suspend fun fetchCategories() : Result<List<CategoryModel?>>{
        return try {
            val document = mFireStore
                .collection("categories")
                .get()
                .await()

            val list : MutableList<CategoryModel?> = mutableListOf()
            for(data in document.documents){
                list.add(data.toObject(CategoryModel::class.java))
            }

            Result.Success(list.toList())
        } catch (e : Exception){
            Result.Error(e)
        }
    }
}