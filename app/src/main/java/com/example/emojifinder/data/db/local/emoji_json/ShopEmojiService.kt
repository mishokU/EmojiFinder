package com.example.emojifinder.data.db.local.emoji_json

import android.app.Application
import com.example.emojifinder.domain.result.Result
import com.example.emojifinder.ui.application.MainApplication
import com.example.emojifinder.ui.shop.EmojiShopModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.android.support.DaggerApplication
import java.lang.Exception
import javax.inject.Inject

class ShopEmojiService @Inject constructor(val application: Application) {

    fun fetchEmojis(): Result<List<EmojiShopModel>> {
        return try {
            val jsonFileString = getJsonDataFromAsset(application, "emoji.json")
            val gson = Gson()
            val listEmojiType = object : TypeToken<List<EmojiShopModel>>() {}.type
            val emojis: List<EmojiShopModel> = gson.fromJson(jsonFileString, listEmojiType)
            Result.Success(emojis)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun fetchDailyEmojis(): Result<List<EmojiShopModel>> {
        return try {
            val jsonFileString = getJsonDataFromAsset(application, "emoji_daily.json")
            val gson = Gson()
            val listEmojiType = object : TypeToken<List<EmojiShopModel>>() {}.type
            val emojis: List<EmojiShopModel> = gson.fromJson(jsonFileString, listEmojiType)
            Result.Success(emojis)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}