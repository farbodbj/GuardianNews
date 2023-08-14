package com.bale_bootcamp.guardiannews.data.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bale_bootcamp.guardiannews.GuardianNewsApp
import com.bale_bootcamp.guardiannews.data.local.database.NewsDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.di.RemoteMediatorAssistedFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject


private const val TAG: String = "NewsRepository"
class NewsRepository @Inject constructor(
    private val localDataSource: NewsDao,
    private val settingsRepository: SettingsRepository
) {

    private var fromDate: LocalDate = LocalDate.now().minusMonths(1)

    @Inject lateinit var remoteMediatorAssistedFactory: RemoteMediatorAssistedFactory

    @OptIn(ExperimentalPagingApi::class)
    suspend fun getNews(category: NewsApiService.Category, toDate: LocalDate): Flow<PagingData<News>> {

        val pagingConfig = getPageConfig()
        val pager = Pager(
            config = pagingConfig,
            remoteMediator = remoteMediatorAssistedFactory.create(category, fromDate, toDate)
        ) {
            val orderBy = runBlocking {
                OrderBy.findByStr(settingsRepository.getOrderBy().first())
            }
            Log.d(TAG, "getting news with page config: $pagingConfig for category: $category, fromDate: $fromDate, toDate: $toDate, ordered by: ${orderBy.value}")
            localDataSource.select(category, orderBy)
        }
        // add caching if feasible
        return pager.flow
    }

    private suspend fun getPageConfig(): PagingConfig {
        val pageSize = settingsRepository.getItemCount().first()
        Log.d(TAG, "setting page config with page size: $pageSize")
        return PagingConfig(
            pageSize = pageSize,
            prefetchDistance = pageSize - 2,
            enablePlaceholders = false
        )
    }
}