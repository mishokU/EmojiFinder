package com.example.emojifinder.data.db.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.emojifinder.data.db.local.converter.StatusConverter
import com.example.emojifinder.data.db.local.models.EmojiShopModelLocal
import com.example.emojifinder.data.db.local.models.SmallLevelModelLocal
import com.example.emojifinder.data.db.local.room.dao.EmojisDao
import com.example.emojifinder.data.db.local.room.dao.LevelMainInfoDao

@Database(
    entities = [EmojiShopModelLocal::class, SmallLevelModelLocal::class],
    version = 3, exportSchema = false
)
@TypeConverters(
    StatusConverter::class
)
abstract class LevelsDatabase : RoomDatabase() {

    abstract fun emojisDao(): EmojisDao
    abstract fun levelDao(): LevelMainInfoDao

    companion object {

        @Volatile
        private var INSTANCE: LevelsDatabase? = null

        fun getDatabase(context: Context): LevelsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LevelsDatabase::class.java,
                        "levels_database"
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