package com.bale_bootcamp.guardiannews.localdatasources.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.bale_bootcamp.guardiannews.localdatasources.BaseDao
import com.bale_bootcamp.guardiannews.model.News
import com.bale_bootcamp.guardiannews.network.NewsApiService
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao: BaseDao<News> {
    @Query("select * from ${News.ENTITY_NAME} where sectionId = :category limit :count")
    suspend fun selectInternal(count: Int, category: String): Flow<List<News>>

    @Transaction
    suspend fun select(count: Int, category: NewsApiService.Category): Flow<List<News>> {
        return selectInternal(count, category.name)
    }
}