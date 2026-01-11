package com.paraspatil.readstack.data.repository

import com.paraspatil.readstack.data.local.BookDao
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.remote.GoogleBookApi
import com.paraspatil.readstack.data.remote.toEntity
import com.paraspatil.readstack.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    private val api: GoogleBookApi,
    private val dao: BookDao
) : BookRepository {

    override fun getLibrary(): Flow<List<BookEntity>> {
        return dao.getAllBooks()
    }

    override suspend fun searchBooks(query: String): Result<Unit> {
        return try {
            val response = api.searchBooks(query)
            val bookEntities = response.items?.map { it.toEntity() } ?: emptyList()

           if(bookEntities.isNotEmpty()){
               dao.upsertBook(bookEntities)
           }
            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteBook(book: BookEntity) {
        dao.deleteBook(book)
    }
}