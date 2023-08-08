package com.bale_bootcamp.guardiannews.data.local.model

import androidx.room.Entity

@Entity(tableName = "remote_keys", primaryKeys = ["id"])
data class NewsRemoteKey(
    val id: String,
    val nextKey: Int?
)
