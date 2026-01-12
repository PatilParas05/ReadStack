package com.paraspatil.readstack.data.repository

import androidx.compose.foundation.interaction.HoverInteraction
import com.paraspatil.readstack.data.local.BookDao
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import com.paraspatil.readstack.data.remote.GoogleBookApi
import com.paraspatil.readstack.data.remote.toSearchResultEntity
import com.paraspatil.readstack.data.util.NetworkMonitor
import com.paraspatil.readstack.domain.repository.BookRepository
import com.paraspatil.readstack.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBookApi,
    private val dao: BookDao,
    private val networkMonitor: NetworkMonitor
) : BookRepository {

    override fun getLibrary(): Flow<List<BookEntity>> {
        return dao.getAllBooks()
    }

    override fun getBookById(bookId: String): Flow<BookEntity?> {
        return  dao.getBookById(bookId)
    }

    override suspend fun addBookToLibrary(book: BookEntity) {
        dao.upsertBooks(book)
    }

    override suspend fun deleteBook(book: BookEntity) {
        dao.upsertBooks(book)
    }

    override suspend fun clearLibrary() {
        dao.clearAllBooks()

    }
    override fun getSearchResults(query: String): Flow<List<SearchResultEntity>> {
        return dao.getSearchResults(query)
    }

    override suspend fun searchBooks(query: String): NetworkResult<Unit> {
        if (!networkMonitor.isCurrentlyOnline()) {
            return NetworkResult.Offline<Unit>()
        }
        return try {
            val response = api.searchBooks(query, maxResults = 40)
            dao.clearSearchResults(query)

            response.items?.forEach { item ->
               val searchResult = item.toSearchResultEntity(query)
                dao.upsertSearchResult(searchResult)
            }
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(
                    message =e.message?:"An unknown error occurred.",
              exception = e
            )
        }
    }

    override suspend fun clearSearchCache(query: String) {
        dao.clearSearchResults(query)
    }

    override suspend fun syncLibrary(): Result<Unit> {
        return try {
            if (!networkMonitor.isCurrentlyOnline()) {
                return Result.failure(Exception("No internet connection"))
            }

            val sevenDaysAgo=System.currentTimeMillis()-(7*24*60*1000)
            dao.clearExpiredSearchResults(sevenDaysAgo)

            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun isOnline(): Flow<Boolean> {
        return networkMonitor.isOnline
    }
}