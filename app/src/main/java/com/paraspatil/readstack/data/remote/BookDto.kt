package com.paraspatil.readstack.data.remote

import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    val items: List<BookItemDto>?=null
)

@Serializable
data class BookItemDto(
    val id: String,
    val volumeInfo: VolumeInfoDto
)
@Serializable
data class VolumeInfoDto(
    val title: String,
    val authors: List<String> ? = null,
    val description: String? = null,
    val imageLinks: ImageLinksDto? = null,
    val pageCount: Int? = null,
    val publishedDate: String? = null,
    val previewLink: String? = null

)
@Serializable
data class ImageLinksDto(
    val thumbnail: String? = null
)

fun BookItemDto.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = volumeInfo.title,
        author = volumeInfo.authors?.joinToString(", ")?:"Unknown Author",
        thumbnailUrl = volumeInfo.imageLinks?.thumbnail ?.replace("http:","https:")?:"",
        description = volumeInfo.description,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate,
        previewLink = volumeInfo.previewLink


    )
}

fun BookItemDto.toSearchResultEntity(searchQuery: String): SearchResultEntity {
    return SearchResultEntity(
        id = id,
        title = volumeInfo.title,
        author = volumeInfo.authors?.joinToString(", ")?:"Unknown Author",
        thumbnailUrl = volumeInfo.imageLinks?.thumbnail ?.replace("http:","https:")?:"",
        description = volumeInfo.description,
        pageCount = volumeInfo.pageCount,
        publishedDate = volumeInfo.publishedDate,
        searchQuery = searchQuery,
        previewLink = volumeInfo.previewLink
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
    publishedDate = publishedDate,
        previewLink = previewLink
    )
}
