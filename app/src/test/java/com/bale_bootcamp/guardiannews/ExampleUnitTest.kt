package com.bale_bootcamp.guardiannews

import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun apiTest() {
        for(category in TestUtils().categoryIterator()) {
            val randomDatePair = TestUtils().randomDateGenerator()
            NewsApi.retrofitApiService.getLatestFromCategory(category, randomDatePair.first, randomDatePair.second, 1).execute().body()?.let {
                assertEquals(it.status, "ok")
            }
        }
    }
}