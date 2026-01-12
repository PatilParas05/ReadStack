package com.paraspatil.readstack.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.paraspatil.readstack.data.remote.GoogleBookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGoogleBookApi(): GoogleBookApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(GoogleBookApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(GoogleBookApi::class.java)
    }
}
