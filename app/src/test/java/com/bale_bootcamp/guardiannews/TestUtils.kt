package com.bale_bootcamp.guardiannews

import com.bale_bootcamp.guardiannews.network.NewsApiService
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.Date
import java.util.GregorianCalendar
import java.util.Random

class TestUtils {

    // returns two random dates in the format "yyyy-MM-dd" where the first date is before the second date
    fun randomDateGenerator(): Pair<LocalDate, LocalDate> {
        val random = Random()
        val startYear = Year.of(random.nextInt(31) + 1990) // Random year between 1990 and 2020
        val startMonth = Month.of(random.nextInt(12) + 1) // Random month
        val startDayOfMonth = random.nextInt(startMonth.length(startYear.isLeap)) + 1 // Random day of month
        val startDate = LocalDate.of(startYear.value, startMonth, startDayOfMonth)

        val endYear = Year.of(random.nextInt(31) + 1990) // Random year between 1990 and 2020
        val endMonth = Month.of(random.nextInt(12) + 1) // Random month
        val endDayOfMonth = random.nextInt(endMonth.length(endYear.isLeap)) + 1 // Random day of month
        var endDate = LocalDate.of(endYear.value, endMonth, endDayOfMonth)

        // Ensure that end date is after start date
        while (endDate.isBefore(startDate)) {
            endDate = endDate.plusDays(1)
        }

        return Pair(startDate, endDate)
    }

    // returns an iterator for the fields in Category enum
    fun categoryIterator(): Iterator<NewsApiService.Category> {
        return NewsApiService.Category.values().iterator()
    }
}