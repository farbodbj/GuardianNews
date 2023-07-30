package com.bale_bootcamp.guardiannews.utility

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


object MoshiInstance {
    private val builder: Moshi.Builder = Moshi.Builder().add(KotlinJsonAdapterFactory())

    fun <T> registerTypeAdapter(type: Class<T>, typeAdapter: JsonAdapter<T>): MoshiInstance {
        builder.add(type, typeAdapter)
        return this
    }

    val instance: Moshi by lazy {
        builder.build()
    }
}