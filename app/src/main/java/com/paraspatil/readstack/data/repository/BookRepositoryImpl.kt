package com.paraspatil.readstack.data.repository

import com.paraspatil.readstack.data.local.BookDao
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.remote.GoogleBookApi
import com.paraspatil.readstack.data.remote.toEntity
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.domain.model.toDomain
import com.paraspatil.readstack.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBookApi,
    private val dao: BookDao
) : BookRepository {

    override fun getLibrary(): Flow<List<Book>> {
        return dao.getAllBooks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun searchBooks(query: String): Result<Unit> {
        return try {
            val response = api.searchBooks(query, maxResults = 40)
            val bookEntities = response.items?.map { it.toEntity() } ?: emptyList()

           if(bookEntities.isNotEmpty()){
               bookEntities.forEach { book->
                   dao.upsertBooks(book)
               }
           }
            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteBook(bookId: String) {
        dao.deleteBook(bookId)

    }
}