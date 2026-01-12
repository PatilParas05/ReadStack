package com.paraspatil.readstack.domain.repository

import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import com.paraspatil.readstack.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getLibrary(): Flow<List<BookEntity>>
    fun getBookById(bookId: String): Flow<BookEntity?>
    suspend fun addBookToLibrary(book: BookEntity)
    suspend fun deleteBook(book: bookEntity)
    suspend fun clearLibrary()
    fun getSearchResults(query: String): Flow<List<SearchResultEntity>>
    suspend fun searchBooks(query: String): NetworkResult<Unit>
    suspend fun clearSearchCache(query: String)
    suspend fun syncLibrary(): Result<Unit>
    fun isOnline(): Flow<Boolean>
}