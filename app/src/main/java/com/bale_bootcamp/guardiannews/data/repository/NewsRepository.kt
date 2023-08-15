package com.bale_bootcamp.guardiannews.data.repository


import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bale_bootcamp.guardiannews.data.local.database.NewsDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.di.RemoteMediatorAssistedFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
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
        val orderBy = runBlocking {
            OrderBy.findByStr(settingsRepository.getOrderBy().first())
        }
        val pagingConfig = getPageConfig()
        val pager = Pager(
            config = pagingConfig,
            remoteMediator = remoteMediatorAssistedFactory.create(category, fromDate, toDate, orderBy)
        ) {

            Log.d(TAG, "getting news with page config: $pagingConfig for category: $category, fromDate: $fromDate, toDate: $toDate, ordered by: ${orderBy.value}")
            localDataSource.select(category)
        }
        // add caching if feasible
        return pager.flow
    }

    private suspend fun getPageConfig(): PagingConfig {
        val pageSize = settingsRepository.getItemCount().first()
        Log.d(TAG, "setting page config with page size: $pageSize")
        return PagingConfig(
            pageSize = pageSize,
            prefetchDistance = pageSize,
            enablePlaceholders = false
        )
    }
}