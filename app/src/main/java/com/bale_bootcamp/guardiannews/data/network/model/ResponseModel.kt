package com.bale_bootcamp.guardiannews.data.network.model

import com.bale_bootcamp.guardiannews.data.local.model.News

data class ResponseModel(
    val status: String,
    val pageSize: Int,
    val total: Int,
    val currentPage: Int,
    val pages: Int,
    val results: List<News>)
