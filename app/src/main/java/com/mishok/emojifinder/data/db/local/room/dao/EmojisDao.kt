package com.mishok.emojifinder.data.db.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mishok.emojifinder.data.db.local.models.EmojiShopModelLocal

@Dao
interface EmojisDao {

    @Insert
    fun insert(albumLocalModel: EmojiShopModelLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<EmojiShopModelLocal>)

    @Query("Select * from levels_table where levelTitle = :title")
    fun getEmojis(title: String): LiveData<List<EmojiShopModelLocal>>

    @Query("Delete from levels_table")
    fun deleteAll()

    @Query("Delete from levels_table where levelTitle = :title")
    fun delete(title: String)

}