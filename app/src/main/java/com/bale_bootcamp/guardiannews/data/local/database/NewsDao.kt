package com.bale_bootcamp.guardiannews.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.bale_bootcamp.guardiannews.data.local.BaseDao
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.ui.settings.model.OrderBy
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao: BaseDao<News> {
    companion object {
        var lastUpdated: Long = 0L
    }

    @Query("select count(*) from ${News.ENTITY_NAME}")
    fun count(): Flow<Int>

    @Query("select * from ${News.ENTITY_NAME}")
    fun selectAll(): PagingSource<Int, News>

    @Query("select * from ${News.ENTITY_NAME} where sectionId = :category")
    fun selectByCategory(category: String): PagingSource<Int, News>

    fun select(category: NewsApiService.Category): PagingSource<Int, News> {
        return if (category == NewsApiService.Category.HOME) {
            selectAll()
        } else {
            selectByCategory(category.categoryName)
        }
    }

    @Query("delete from ${News.ENTITY_NAME} where sectionId = :category")
    suspend fun deleteByCategory(category: String): Int

    @Query("delete from ${News.ENTITY_NAME} where sectionId not in (:categories)")
    suspend fun deleteExcept(categories: List<String>): Int

    suspend fun delete(category: NewsApiService.Category): Int =
        if(category != NewsApiService.Category.HOME)
            deleteByCategory(category.categoryName)
        else
            deleteExcept(NewsApiService.Category.categoryNameList())


}
