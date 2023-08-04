package com.bale_bootcamp.guardiannews.pagination

import com.bale_bootcamp.guardiannews.network.NewsApiService
import java.time.LocalDate

data class NewsKey(
    val category: NewsApiService.Category,
    val fromDate: LocalDate = LocalDate.now().minusMonths(1),
    val toDate: LocalDate = LocalDate.now(),
    val page: Int,
    val pageSize: Int = 10,
) {
    operator fun plus(num: Int) = copy(page = page + num)
    operator fun minus(num: Int) = copy(page = page - num)
}
