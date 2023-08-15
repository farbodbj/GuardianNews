package com.bale_bootcamp.guardiannews.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bale_bootcamp.guardiannews.data.local.database.NewsDao
import com.bale_bootcamp.guardiannews.data.local.database.RemoteKeyDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.local.model.NewsRemoteKey
import com.bale_bootcamp.guardiannews.data.network.model.ResponseModel
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "NewsPagingMediator"
@OptIn(ExperimentalPagingApi::class)

class NewsPagingMediator @AssistedInject constructor(
    private val onlineDataSource: NewsApiService,
    private val localNewsDataSource: NewsDao,
    private val localRemoteKeyDataSource: RemoteKeyDao,
    private val localSettingsRepository: SettingsRepository,
    @Assisted private val category: NewsApiService.Category,
    @Assisted("from-date") private val fromDate: LocalDate,
    @Assisted("to-date") private val toDate: LocalDate,
    @Assisted private val orderBy: OrderBy,
) : RemoteMediator<Int, News>() {

    private var lastUpdated: Long = 0L

    override suspend fun initialize(): InitializeAction {
        Log.d(TAG, "initialize called")
        return if(lastUpdated == 0L) {
            lastUpdated = System.currentTimeMillis()
            Log.d(TAG, "initialize: lastUpdated: $lastUpdated")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            val diff = System.currentTimeMillis() - lastUpdated
            if(TimeUnit.MILLISECONDS.toHours(diff) < 1) {
                Log.d(TAG, "initialize: diff: $diff, minutes: ${TimeUnit.MILLISECONDS.toHours(diff)}")
                Log.d(TAG, "initialize: skipping initial refresh")
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                Log.d(TAG, "initialize: diff: $diff, minutes: ${TimeUnit.MILLISECONDS.toHours(diff)}")
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {
        Log.d(TAG, "load called")
        val page: Int?  =
            try {
                Log.d(TAG, "load type: ${loadType.name}")
                when(loadType) {
                    LoadType.REFRESH -> null
                    LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    LoadType.APPEND -> {
                        if(state.isEmpty()) {
                            return MediatorResult.Success(endOfPaginationReached = false)
                        }
                        else {
                            val lastAccessedId = state.lastItemOrNull()?.id
                            Log.d(TAG, "lastAccessedId: $lastAccessedId")
                            if (lastAccessedId == null) {
                                Log.d(TAG, "lastAccessedId is null")
                                1
                            } else {
                                Log.d(TAG, "lastAccessedId is not null")
                                val last =
                                    localRemoteKeyDataSource.getRemoteKeyById(lastAccessedId)?.nextKey
                                Log.d(TAG, "last item remote key next key: ${last}")
                                if (last != null)
                                    last + 1
                                else
                                    1
                            }
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e(TAG, "exception: ${e.message}")
            return MediatorResult.Error(e)
        }

        val response = getFromApi(page ?: 1)
        Log.d(TAG, "got data for page: ${response.currentPage} from api")

        //System.currentTimeMillis() - lastUpdated > 3600000
        if(loadType == LoadType.REFRESH) {
            val remoteKeyDeletes = localRemoteKeyDataSource.delete(category)
            val newsDeletes = localNewsDataSource.delete(category)
            Log.d(TAG, "for categoey: ${category.categoryName}, remote keys deleted: $remoteKeyDeletes, news deleted: $newsDeletes")
        }

        addRemoteKeys(response)
        insertToNewsDb(response.results)
        val hasMoreData = response.pages > response.currentPage + 1
        Log.d(TAG, "response pages: ${response.pages}, current page: ${response.currentPage}, has more data: $hasMoreData")

        return MediatorResult.Success(endOfPaginationReached = !hasMoreData)
    }

    private suspend fun getFromApi(lastAccessedPage: Int) = try {
        Log.d(TAG, "getFromApi: ${category.categoryName}, $fromDate, $toDate, $lastAccessedPage")
        onlineDataSource.getLatestFromCategory(
            category,
            fromDate,
            toDate,
            lastAccessedPage,
    50,
            orderBy.value).response
        } catch (e: Exception) {
            Log.e(TAG, "exception: ${e.message}")
            ResponseModel("status", 0, 0, 10, 50, emptyList())
        }


    private suspend fun insertToNewsDb(news: List<News>) = localNewsDataSource.insertAll(*news.toTypedArray())

    private suspend fun addRemoteKeys(response: ResponseModel) {
        for(news in response.results) {
            val newsRemoteKey = NewsRemoteKey(news.id, response.currentPage + 1)
            localRemoteKeyDataSource.insert(newsRemoteKey)
        }
    }
}