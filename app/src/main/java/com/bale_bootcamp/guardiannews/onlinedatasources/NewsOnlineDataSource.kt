package com.bale_bootcamp.guardiannews.onlinedatasources

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

    fun getNews(category: NewsApiService.Category, fromDate: LocalDate, toDate: LocalDate, page: Int, pageSize: Int): MutableLiveData<ResponseModel?> {
        lateinit var responseModel: MutableLiveData<ResponseModel?>
        apiService.getLatestFromCategory(category, fromDate, toDate, page, pageSize)
            .enqueue(object : Callback<NetworkResponse> {
                override fun onResponse(
                    call: retrofit2.Call<NetworkResponse>,
                    response: retrofit2.Response<NetworkResponse>
                ) {
                    if (response.isSuccessful) {
                        responseModel = MutableLiveData(response.body()?.response)
                    } else {
                        TODO("log the error")
                    }
                }

                override fun onFailure(call: retrofit2.Call<NetworkResponse>, t: Throwable) {
                    TODO("log the error")
                }
            })
        return responseModel
    }
}
