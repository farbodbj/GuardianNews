package com.bale_bootcamp.guardiannews.utility

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class LocalDateTimeAdapter: JsonAdapter<LocalDateTime>() {
    companion object {
        val isoFormatter = DateTimeFormatter.ISO_DATE_TIME
        val casualFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
    }
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        return LocalDateTime.parse(reader.nextString(), isoFormatter)
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        writer.value(value?.format(isoFormatter))
    }
}
