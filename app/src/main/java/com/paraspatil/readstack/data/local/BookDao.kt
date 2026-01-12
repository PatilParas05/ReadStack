package com.paraspatil.readstack.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM readstackbook ORDER BY timestamp DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM readstackbook WHERE id = :bookId")
    fun getBookById(bookId: String): Flow<BookEntity?>

    @Upsert
    suspend fun upsertBooks(book: List<BookEntity>)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM readstackbook ")
    suspend fun clearAllBooks()

}