package com.bale_bootcamp.guardiannews.data.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.data.local.database.NewsDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import java.time.LocalDate

class NewsRepository (
    private val onlineDataSource: NewsApiService,
    private val localDataSource: NewsDao,
    private val settingsRepository: SettingsRepository
) {
    private val TAG: String = "NewsRepository"

    private var pageSize: Int = 10
    private var fromDate: LocalDate = LocalDate.now().minusMonths(1)

    private val pagingConfig: PagingConfig by lazy {
        setPageSize()
        PagingConfig(
            pageSize = pageSize,
            prefetchDistance = pageSize - 2,
            enablePlaceholders = false
        )
    }

    private fun setPageSize() {
        CoroutineScope(Dispatchers.IO).launch {
            settingsRepository.getItemCount().collect {
                Log.d(TAG, "getPageSize collected from preferences datastore: $it")
                pageSize = it
            }
        }
    }

    suspend fun refreshNews(category: NewsApiService.Category,
                            toDate: LocalDate,
                            page: Int) {

        val results = onlineDataSource.getLatestFromCategory(category,
            fromDate,
            toDate,
            page,
            pageSize
        ).response

        results.results.let {
            Log.d(TAG, it.toString())
            localDataSource.insertAll(*it.toTypedArray())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getNews(category: NewsApiService.Category, toDate: LocalDate): Flow<PagingData<News>> {

        val pager = Pager(
            config = pagingConfig,
            remoteMediator = NewsPagingMediator(onlineDataSource,
                localDataSource,
                GuardianNewsApp.getAppContext().database.remoteKeyDao(),
                category,
                fromDate,
                toDate)
        ) {
            Log.d(TAG, "getting news with page config: $pagingConfig for category: $category, fromDate: $fromDate, toDate: $toDate")
            localDataSource.select(category)
        }

        // add caching if feasible
        return pager.flow
    }
}