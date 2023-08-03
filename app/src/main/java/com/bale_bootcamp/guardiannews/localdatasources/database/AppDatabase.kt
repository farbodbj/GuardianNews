package com.bale_bootcamp.guardiannews.localdatasources.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.utility.LocalDateTimeConverter
import java.time.LocalDateTime
import java.util.concurrent.Executor

@Database(entities = [News::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        private const val DATABASE_NAME = "app_database"
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, queryCallback: QueryCallback, queryExecutor: Executor): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                if(INSTANCE != null) INSTANCE!!
                else {
                    val instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .setQueryCallback(queryCallback, queryExecutor)
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}