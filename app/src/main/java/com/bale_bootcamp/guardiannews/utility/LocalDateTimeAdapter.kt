package com.bale_bootcamp.guardiannews.utility

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter: JsonAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        return LocalDateTime.parse(reader.nextString(), formatter)
    }

    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.format(formatter))
    }
}
