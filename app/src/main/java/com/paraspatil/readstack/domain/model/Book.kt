package com.paraspatil.readstack.domain.model

import com.paraspatil.readstack.data.local.BookEntity

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val thumbnailUrl: String,
    val description: String = "",
    val pageCount: Int = 0,
    val publishedDate: String = ""
)

fun BookEntity.toDomain(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        thumbnailUrl = thumbnailUrl,
        description = description ?: "",
        pageCount = pageCount ?: 0,
        publishedDate = publishedDate ?: ""
    )
}
