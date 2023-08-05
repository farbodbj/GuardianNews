package com.bale_bootcamp.guardiannews.data.local.model


import androidx.room.Embedded
import androidx.room.Entity
import com.squareup.moshi.Json
import java.time.LocalDateTime

@Entity(tableName = News.ENTITY_NAME, primaryKeys = ["id"])
data class News (
    val id: String,
    val type: String,
    val sectionId: String,
    val sectionName: String,
    val webPublicationDate: LocalDateTime,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    @Json(name = "fields")
    @Embedded
    val details: NewsDetail
) {
    companion object {
        const val ENTITY_NAME = "news"
    }
}


