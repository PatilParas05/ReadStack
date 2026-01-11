package com.paraspatil.readstack.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBookApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("printType") printType: String = "books"
    ): BookResponseDto

    companion object{
        const val BASE_URL="https://www.googleapis.com/books/v1/"

    }

}