package com.example.emojifinder.data.db.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.emojifinder.data.db.local.models.EmojiShopModelLocal
import com.example.emojifinder.data.db.local.room.dao.EmojisDao

@Database(entities = [EmojiShopModelLocal::class],
    version = 1, exportSchema = false)
abstract class LevelsDatabase : RoomDatabase() {

    abstract fun emojisDao(): EmojisDao

    companion object {

        @Volatile
        private var INSTANCE: LevelsDatabase? = null

        fun getDatabase(context: Context): LevelsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LevelsDatabase::class.java,
                        "levels_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}