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
    suspend fun upsertBooks(book: BookEntity)

    @Query("DELETE FROM readstackbook ")
    suspend fun clearAllBooks()

    @Query("DELETE FROM search_result WHERE searchQuery = :query ORDER BY timestamp DESC LIMIT 5")
    fun getSearchResults(query: String):Flow<List<SearchResultEntity>>

    @Upsert
    suspend fun upsertSearchResult(result: SearchResultEntity)

    @Query("DELETE FROM search_result WHERE searchQuery = :query")
    suspend fun clearSearchResults(query: String)

    @Query("DELETE FROM search_result WHERE timestamp <:expirytime")
    suspend fun clearExpiredSearchResults(expirytime:Long)


}