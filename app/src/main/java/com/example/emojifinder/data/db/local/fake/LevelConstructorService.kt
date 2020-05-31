package com.example.emojifinder.data.db.local.fake

import com.example.emojifinder.data.db.remote.models.EmojiShopModel
import com.example.emojifinder.domain.result.Result
import java.lang.Exception


class LevelConstructorService {


    fun fetchFakeData(): Result<List<EmojiShopModel>> {
        return try {
            val cells = mutableListOf<EmojiShopModel>()
            for(i : Int in 0..99){
                val emoji = EmojiShopModel()
                cells.add(emoji)
            }
            Result.Success(cells)
        }  catch (e : Exception){
            Result.Error(e)
        }
    }

}