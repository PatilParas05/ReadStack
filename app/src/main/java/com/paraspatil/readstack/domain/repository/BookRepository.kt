package com.paraspatil.readstack.domain.repository

import com.paraspatil.readstack.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getLibrary(): Flow<List<Book>>
    suspend fun searchBooks(query: String): Result<Unit>

    suspend fun deleteBook(bookId: String)
}