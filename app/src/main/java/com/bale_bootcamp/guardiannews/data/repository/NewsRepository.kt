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
import com.bale_bootcamp.guardiannews.utility.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


private const val TAG: String = "NewsRepository"
class NewsRepository @Inject constructor(
    private val localDataSource: NewsDao,
    private val settingsRepository: SettingsRepository
) {

    @Inject lateinit var remoteMediatorAssistedFactory: RemoteMediatorAssistedFactory

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    fun getNews(category: NewsApiService.Category, toDate: LocalDate = LocalDate.now()): Flow<PagingData<News>> {
        Log.d(TAG, "**** getNews() called! ****")
        val orderByFlow = settingsRepository.getOrderBy()
        val fromDateFlow = settingsRepository.getFromDate()


        val pagerFlow = fromDateFlow.combine(orderByFlow) { fromDateStr, orderByStr ->
            val orderBy = OrderBy.findByStr(orderByStr)
            val fromDate = LocalDate.parse(fromDateStr, Utils.formatter)

            Log.d(TAG, "order by: $orderBy, from date: $fromDate")
            Pager(
                config = getPageConfig(),
                remoteMediator = remoteMediatorAssistedFactory.create(category, fromDate, toDate, orderBy)
            ) {
                Log.d(TAG, "getting news for category: $category, fromDate: $fromDate, toDate: $toDate, ordered by: ${orderBy.value}")
                localDataSource.select(category)
            }
        }

        return pagerFlow.flatMapLatest { pager ->
            pager.flow
        }

    }

    private suspend fun getPageConfig(): PagingConfig {
        val pageSize = settingsRepository.getItemCount().first()
        Log.d(TAG, "setting page config with page size: $pageSize")
        return PagingConfig(
            pageSize = pageSize,
            prefetchDistance = 1,
            enablePlaceholders = false
        )
    }
}