package com.bale_bootcamp.guardiannews.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.bale_bootcamp.guardiannews.data.local.BaseDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy

@Dao
interface NewsDao: BaseDao<News> {
    @Query("select * from ${News.ENTITY_NAME}")
    fun selectAllDefault(): PagingSource<Int, News>

    @Query("select * from ${News.ENTITY_NAME} where sectionId = :category")
    fun selectByCategory(category: String): PagingSource<Int, News>

    @Query("select * from ${News.ENTITY_NAME} order by webPublicationDate desc")
    fun selectAllDesc(): PagingSource<Int, News>

    @Query("select * from ${News.ENTITY_NAME} order by webPublicationDate asc")
    fun selectAllAsc(): PagingSource<Int, News>

    fun select(category: NewsApiService.Category, orderBy: OrderBy): PagingSource<Int, News> {
        return if (category == NewsApiService.Category.HOME) {
            when(orderBy) {
                OrderBy.NEWEST -> selectAllDesc()
                OrderBy.OLDEST -> selectAllAsc()
                OrderBy.RELEVANCE -> selectAllDefault()
            }
        } else {
            selectByCategory(category.categoryName)
        }
    }

    @Query("delete from ${News.ENTITY_NAME} where sectionId = :category")
    suspend fun deleteByCategory(category: String): Int

    suspend fun delete(category: NewsApiService.Category): Int {
        return deleteByCategory(category.categoryName)
    }
}
