package com.example.emojifinder.data.db.remote.api

import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.categories.CategoryModel

interface FirebaseCategories {
    suspend fun fetchCategories() : Result<List<CategoryModel?>>
}