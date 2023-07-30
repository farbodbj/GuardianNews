package com.bale_bootcamp.guardiannews.utility

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return formatter.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, formatter)
    }
}
