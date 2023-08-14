package com.bale_bootcamp.guardiannews.di

import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.data.repository.NewsPagingMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import java.time.LocalDate

@AssistedFactory
interface RemoteMediatorAssistedFactory {
    fun create(category: NewsApiService.Category, @Assisted("from-date") fromDate: LocalDate, @Assisted("to-date") toDate: LocalDate): NewsPagingMediator
}