package com.bale_bootcamp.guardiannews.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bale_bootcamp.guardiannews.localdatasources.database.NewsDao
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApiService
import kotlinx.coroutines.flow.single

class NewsPagingSource (private val localDataSource: NewsDao,
                        private val category: NewsApiService.Category,
): PagingSource<NewsKey, News>() {
    override fun getRefreshKey(state: PagingState<NewsKey, News>): NewsKey? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<NewsKey>): LoadResult<NewsKey, News> {
        val key = params.key ?: NewsKey(category = category, page = 1)

        val results = localDataSource.selectAll().single()

        val prevKey = if (key.page > 1) NewsKey(key.category, page = key.page - 1) else null
        val nextKey = if (results.isNotEmpty()) NewsKey(key.category, page = key.page + 1) else null

        return LoadResult.Page(results, prevKey, nextKey)
    }
}