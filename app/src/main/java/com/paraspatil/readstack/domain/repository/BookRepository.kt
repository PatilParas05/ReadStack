package com.paraspatil.readstack.domain.repository

import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getLibrary(): Flow<List<BookEntity>>
    fun getBookById(bookId: String): Flow<BookEntity?>
    suspend fun addBookToLibrary(book: BookEntity)
    suspend fun deleteBook(book: BookEntity)
    suspend fun clearLibrary()


    fun getSearchResults(query: String): Flow<List<SearchResultEntity>>
    suspend fun searchBooks(query: String): NetworkResult<Unit>

    suspend fun searchBooksWithPagination(
        query: String,
        startIndex: Int=0,
        maxResults: Int=20,
        shouldReplace: Boolean = true,
        ): NetworkResult<Unit>
    suspend fun clearSearchCache(query: String)
    suspend fun syncLibrary(): Result<Unit>
    fun isOnline(): Flow<Boolean>
}