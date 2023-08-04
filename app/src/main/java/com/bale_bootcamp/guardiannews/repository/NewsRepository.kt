package com.bale_bootcamp.guardiannews.repository


import android.util.Log
import com.bale_bootcamp.guardiannews.localdatasources.database.NewsDao
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import kotlinx.coroutines.flow.Flow

import java.time.LocalDate

class NewsRepository (
    private val onlineDataSource: NewsOnlineDataSource,
    private val localDataSource: NewsDao
) {
    private val TAG: String = "NewsRepository"

    suspend fun refreshNews(category: NewsApiService.Category,
                    fromDate: LocalDate,
                    toDate: LocalDate,
                    page: Int,
                    pageSize: Int) {

        val results = onlineDataSource.getNews(category, fromDate, toDate, page, pageSize)

        results.value?.results?.let {
            Log.d(TAG, it.toString())
            localDataSource.insertAll(*it.toTypedArray())
        }
    }

    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int
    ): Flow<List<News>> =
        localDataSource.select(category)
}