package com.bale_bootcamp.guardiannews

import android.app.Application
import android.util.Log
import androidx.room.RoomDatabase
import com.bale_bootcamp.guardiannews.data.local.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors.newSingleThreadExecutor

@HiltAndroidApp
class GuardianNewsApp: Application() {
    val name = "GuardianNewsApp"

    val database by lazy {
        AppDatabase.getDatabase(
                this,
                object : RoomDatabase.QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        Log.d("Database", "query: $sqlQuery\n args:$bindArgs")
                    }
                },
                newSingleThreadExecutor()
            )
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private lateinit var appContext: GuardianNewsApp
        fun getAppContext(): GuardianNewsApp {
            return appContext
        }
    }
}