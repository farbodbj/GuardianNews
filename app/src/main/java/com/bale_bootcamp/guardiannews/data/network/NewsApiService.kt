package com.bale_bootcamp.guardiannews.data.network

import com.bale_bootcamp.guardiannews.data.network.model.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate



object Api {
    const val BASE_URL = "https://content.guardianapis.com/"
    const val API_KEY = "YOUR_OWN_API_KEY"
    const val DEFAULT_FIELDS = "headline,thumbnail,body,trailText,starRating"
}

interface NewsApiService {
    enum class Category(val categoryName: String) {
        HOME("search"),
        WORLD("world"),
        ENVIRONMENT("environment"),
        SCIENCE("science"),
        SPORT("sport");
        override fun toString(): String = this.categoryName
        companion object {
            fun findByStr(value: String): Category {
                return values().find { it.categoryName == value } ?: throw IllegalArgumentException("value not found in enum")
            }

            fun categoryNameList(): List<String> = values().map { it.categoryName }
        }
    }

    @GET("{category}?api-key=${Api.API_KEY}&show-fields=${Api.DEFAULT_FIELDS}")
    suspend fun getLatestFromCategory(@Path("category") category: Category,
                                      @Query("from-date") fromDate: LocalDate,
                                      @Query("to-date") toDate: LocalDate,
                                      @Query("page") page: Int,
                                      @Query("page-size") pageSize: Int = 10,
                                      @Query("order-by") orderByStr: String = "relevance"): NetworkResponse
}