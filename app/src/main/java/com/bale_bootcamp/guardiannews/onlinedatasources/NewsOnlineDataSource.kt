package com.bale_bootcamp.guardiannews.onlinedatasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bale_bootcamp.guardiannews.model.NetworkResponse
import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import retrofit2.Callback
import retrofit2.await
import retrofit2.awaitResponse
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

class NewsOnlineDataSource (
    private val apiService: NewsApiService = NewsApi.retrofitApiService
) {
    private val TAG: String = "NewsOnlineDataSource"

    suspend fun getNews(category: NewsApiService.Category, fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int
    ): MutableLiveData<ResponseModel?> {

        val responseModel: MutableLiveData<ResponseModel?> = MutableLiveData(null)
        Log.d(TAG, "${::getNews.name} called with values category: ${category.name}, fromDate: $fromDate, toDate: $toDate, page: $page, pageSize: $pageSize")

        val result = apiService.getLatestFromCategory(category, fromDate, toDate, page, pageSize).response
        responseModel.postValue(result)

        Log.d(TAG, "getNews return value: $result" )
        return responseModel
    }
}
