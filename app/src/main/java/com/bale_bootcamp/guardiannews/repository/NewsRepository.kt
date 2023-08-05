package com.bale_bootcamp.guardiannews.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.localdatasources.database.NewsDao
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.pagination.NewsPagingMediator
import com.bale_bootcamp.guardiannews.pagination.RemoteKeyDao
import kotlinx.coroutines.flow.Flow

import java.time.LocalDate

class NewsRepository (
    private val onlineDataSource: NewsApiService,
    private val localDataSource: NewsDao
) {
    private val TAG: String = "NewsRepository"

    suspend fun refreshNews(category: NewsApiService.Category,
                    fromDate: LocalDate,
                    toDate: LocalDate,
                    page: Int,
                    pageSize: Int) {

        val results = onlineDataSource.getLatestFromCategory(category, fromDate, toDate, page, pageSize).response
        results.results.let {
            Log.d(TAG, it.toString())
            localDataSource.insertAll(*it.toTypedArray())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getNews(category: NewsApiService.Category,
                fromDate: LocalDate,
                toDate: LocalDate,
                page: Int,
                pageSize: Int
    ): Flow<PagingData<News>> {
        val pagingConfig = PagingConfig(
            pageSize = 10,
            prefetchDistance = 8,
            enablePlaceholders = false
        )

        val pager = Pager(
            config = pagingConfig,
            remoteMediator = NewsPagingMediator(onlineDataSource,
                localDataSource,
                GuardianNewsApp.getAppContext().database.remoteKeyDao(),
                category,
                fromDate,
                toDate)) {
            Log.d(TAG, "localDataSource.selectAll()")
            localDataSource.select(category)
        }

        // add caching if feasible
        return pager.flow
    }
}