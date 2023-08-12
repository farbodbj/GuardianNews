package com.bale_bootcamp.guardiannews.di

import com.bale_bootcamp.guardiannews.BuildConfig
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.utility.MoshiInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkServiceModule {
    private const val BASE_URL = "https://content.guardianapis.com/"

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient =
        if(BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()
        } else {
            OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .build()
        }

    @Provides
    @Singleton
    fun provideConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create(MoshiInstance.instance)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService = retrofit.create(NewsApiService::class.java)
}