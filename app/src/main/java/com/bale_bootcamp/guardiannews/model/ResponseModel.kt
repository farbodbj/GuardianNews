package com.bale_bootcamp.guardiannews.model

data class ResponseModel(
    val status: String,
    val pageSize: Int,
    val total: Int,
    val currentPage: Int,
    val pages: Int,
    val results: List<News>)
