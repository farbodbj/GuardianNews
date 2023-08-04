package com.bale_bootcamp.guardiannews.pagination

import com.bale_bootcamp.guardiannews.network.NewsApiService

data class NewsKey(
    private val category: NewsApiService.Category,
    private val fromDate: String,
    private val toDate: String,
    private val page: Int,
    private val pageSize: Int
)
