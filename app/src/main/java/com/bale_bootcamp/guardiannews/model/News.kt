package com.bale_bootcamp.guardiannews.model


import com.squareup.moshi.Json
import java.time.LocalDateTime

data class News (
    val id: String,
    val type: String,
    val sectionId: String,
    val sectionName: String,
    val webPublicationDate: LocalDateTime,
    val webTitle: String,
    val webUrl: String,
    val apiUrl: String,
    @Json(name = "fields") val details: NewsDetail
)
