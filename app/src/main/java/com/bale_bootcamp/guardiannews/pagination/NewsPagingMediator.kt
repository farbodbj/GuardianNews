package com.bale_bootcamp.guardiannews.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bale_bootcamp.guardiannews.localdatasources.database.NewsDao
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApiService
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class NewsPagingMediator(
    private val onlineDataSource: NewsApiService,
    private val localDataSource: NewsDao,
    private val key: NewsKey
) : RemoteMediator<NewsKey, News>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<NewsKey, News>
    ): MediatorResult {
        return try {
            if(loadType == LoadType.REFRESH || loadType == LoadType.PREPEND) {
                localDataSource.deleteAll()
                MediatorResult.Success(endOfPaginationReached = true)

            } else {
                val results = getFromApi(key).results
                insertToDb(results)
                return MediatorResult.Success(endOfPaginationReached = results.isEmpty())
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getFromApi(apiParams: NewsKey) = with(apiParams) {
        onlineDataSource.getLatestFromCategory(
            category,
            fromDate,
            toDate,
            page,
            pageSize
        ).response
    }

    private suspend fun insertToDb(news: List<News>) = localDataSource.insertAll(*news.toTypedArray())
}