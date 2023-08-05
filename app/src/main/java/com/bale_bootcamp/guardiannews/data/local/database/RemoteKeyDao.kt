package com.bale_bootcamp.guardiannews.data.local.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bale_bootcamp.guardiannews.data.local.model.RemoteKey
import com.bale_bootcamp.guardiannews.data.network.NewsApiService

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("select * from $ENTITY_NAME where id = :id")
    suspend fun getRemoteKeyById(id: String): RemoteKey?

    @Query("delete from $ENTITY_NAME where id like :category + ' || %'")
    suspend fun deleteByCategory(category: String): Int

    suspend fun delete(category: NewsApiService.Category) :Int {
        return deleteByCategory(category.categoryName)
    }

    companion object {
        const val ENTITY_NAME = "remote_keys"
    }
}