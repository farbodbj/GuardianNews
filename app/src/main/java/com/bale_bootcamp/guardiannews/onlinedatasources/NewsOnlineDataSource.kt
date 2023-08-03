package com.bale_bootcamp.guardiannews.onlinedatasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bale_bootcamp.guardiannews.model.NetworkResponse
import com.bale_bootcamp.guardiannews.model.ResponseModel
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import retrofit2.Callback
import java.time.LocalDate

class NewsOnlineDataSource (
    private val apiService: NewsApiService = NewsApi.retrofitApiService
) {
    private val TAG: String = "NewsOnlineDataSource"

    fun getNews(category: NewsApiService.Category, fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int
    ): MutableLiveData<ResponseModel?> {

        val responseModel: MutableLiveData<ResponseModel?> = MutableLiveData(null)
        Log.d(TAG, "${::getNews.name} called with values category: ${category.name}, fromDate: $fromDate, toDate: $toDate, page: $page, pageSize: $pageSize")

        apiService.getLatestFromCategory(category, fromDate, toDate, page, pageSize)
            .enqueue(object : Callback<NetworkResponse> {
                override fun onResponse(
                    call: retrofit2.Call<NetworkResponse>,
                    response: retrofit2.Response<NetworkResponse>
                ) {
                    if (response.isSuccessful) {
                        responseModel.value = response.body()?.response
                        Log.d(TAG, "request successful with code: ${response.code()}, ${response.body()?.response?.results?.size} results returned")
                    } else {
                        Log.d(TAG, "request not successful with code: ${response.code()}")
                        //TODO("log the error")
                    }
                }

                override fun onFailure(call: retrofit2.Call<NetworkResponse>, t: Throwable) {
                    Log.d(TAG, "request failed with ${t.localizedMessage}")
                    //TODO("log the error")
                }
            })

        Log.d(TAG, "getNews return value: ${responseModel.value}" )
        return responseModel
    }
}
