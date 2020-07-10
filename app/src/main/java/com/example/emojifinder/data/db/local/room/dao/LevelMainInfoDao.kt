package com.example.emojifinder.data.db.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.emojifinder.data.db.local.models.SmallLevelModelLocal

@Dao
interface LevelMainInfoDao {

    @Insert
    fun insert(level: SmallLevelModelLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<SmallLevelModelLocal>)

    @Query("Select * from small_levels_table")
    fun getLevels(): LiveData<List<SmallLevelModelLocal>>

    @Query("Delete from small_levels_table")
    fun deleteAll()

    @Query("Delete from small_levels_table where title = :title")
    fun delete(title: String)

}