package com.paraspatil.readstack.domain.repository

import com.paraspatil.readstack.data.local.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookRepository{
    fun getLibrary(): Flow<List<BookEntity>>
    suspend fun searchAndSync(query: String): Result<Unit>
    suspend fun deleteBook(book.BookEntity)

}