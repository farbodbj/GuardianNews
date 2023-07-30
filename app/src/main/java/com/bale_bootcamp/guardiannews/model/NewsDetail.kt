package com.bale_bootcamp.guardiannews.model

data class NewsDetail(
    val headline: String,
    val thumbnail: String?,
    val body: String,
    val trailText: String,
    val starRating: Int?)
