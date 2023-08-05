package com.bale_bootcamp.guardiannews.pagination


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.util.query
import com.bale_bootcamp.guardiannews.network.NewsApiService

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("select * from ${RemoteKeyDao.ENTITY_NAME} where id = :id")
    suspend fun getRemoteKeyById(id: String): RemoteKey?

    @Query("delete from ${RemoteKeyDao.ENTITY_NAME} where id like :category + ' || %'")
    suspend fun deleteByCategory(category: String): Int

    suspend fun delete(category: NewsApiService.Category) :Int {
        return deleteByCategory(category.categoryName)
    }

    companion object {
        const val ENTITY_NAME = "remote_keys"
    }
}