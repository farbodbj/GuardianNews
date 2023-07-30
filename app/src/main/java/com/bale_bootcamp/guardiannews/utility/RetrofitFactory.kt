package com.bale_bootcamp.guardiannews.utility

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

object RetrofitFactory {
    fun createRetrofitInstance(baseUrl: String,
                               client: OkHttpClient,
                               converterFactory: Converter.Factory): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }
}