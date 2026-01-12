package com.paraspatil.readstack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_results")
data class SearchResultEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val description: String?,
    val pageCount: Int?,
    val publishedDate: String?,
    val searchQuery: String,
    val timestamp: Long=System.currentTimeMillis()

)