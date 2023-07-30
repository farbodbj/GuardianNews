package com.bale_bootcamp.guardiannews.network

import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.utility.RetrofitFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.util.Date

object Api {
    const val BASE_URL = "https://content.guardianapis.com/"
    const val API_KEY = "24214983-e02f-49e4-94be-663d934bceb0"
}


interface NewsApiService {
    enum class Category(private val categoryName: String) {
        HOME("search"),
        WORLD("world"),
        SCIENCE("science"),
        ENVIRONMENT("environment"),
        SPORT("sport");
        override fun toString(): String = this.categoryName
    }

    @GET("{category}?api-key=${Api.API_KEY}")
    fun getLatestFromCategory(@Path("category") category: Category,
                              @Query("from-date") fromDate: LocalDate,
                              @Query("to-date") toDate: LocalDate,
                              @Query("page") page: Int,
                              @Query("page-size") pageSize: Int = 10): Call<ResponseModel>
}


object NewsApi {
    private val okhttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val converterFactory: MoshiConverterFactory = MoshiConverterFactory.create()

    val retrofit: Retrofit = RetrofitFactory.createRetrofitInstance(
        baseUrl = Api.BASE_URL,
        client = okhttpClient,
        converterFactory = converterFactory)

    val retrofitApiService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}