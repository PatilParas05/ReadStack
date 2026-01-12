package com.paraspatil.readstack.data.remote

import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity

data class BookResponseDto(
    val items: List<BookItemDto>
)

data class BookItemDto(
    val id: String,
    val volumeInfo: VolumeInfoDto
)

data class VolumeInfoDto(
    val title: String,
    val authors: List<String>,
    val description: String?,
    val imageLinks: ImageLinksDto?,
    val pageCount: Int?,
    val publishedDate: String?
)

data class ImageLinksDto(
    val thumbnail: String
)

fun BookItemDto.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = volumeInfo.title,
        author = volumeInfo.authors.joinToString(", ")?:"Unknown Author",
        thumbnailUrl = volumeInfo.imageLinks?.thumbnail ?.replace("http:","https:")?:"",
        description = volumeInfo.description,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate


    )
}

fun BookItemDto.toSearchResultEntity(searchQuery: String): SearchResultEntity {
    return SearchResultEntity(
        id = id,
        title = volumeInfo.title,
        author = volumeInfo.authors.joinToString(", ")?:"Unknown Author",
        thumbnailUrl = volumeInfo.imageLinks?.thumbnail ?.replace("http:","https:")?:"",
        description = volumeInfo.description,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate,
        searchQuery = searchQuery
    )
}

fun SearchResultEntity.toBookEntity(): BookEntity{
    return BookEntity(
    id = id,
    title = title,
    author = author,
    thumbnailUrl = thumbnailUrl,
    description = description,
    pageCount = pageCount,
    publishedDate = publishedDate
    )
}
