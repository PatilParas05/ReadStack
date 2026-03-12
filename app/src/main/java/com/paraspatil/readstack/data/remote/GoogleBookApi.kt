package com.paraspatil.readstack.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBookApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String = API_KEY,
        @Query("printType") printType: String = "books"
    ): BookResponseDto

    @GET("volumes")
    suspend fun searchBooksWithPagination(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String = API_KEY,
        @Query("printType") printType: String = "books"
    ): BookResponseDto

    companion object{
        const val BASE_URL="https://www.googleapis.com/books/v1/"
        const val API_KEY = ""//Add here Google API key

    }

}