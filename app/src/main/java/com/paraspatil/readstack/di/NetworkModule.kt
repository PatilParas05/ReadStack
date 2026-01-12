package com.paraspatil.readstack.di

import com.paraspatil.readstack.data.remote.GoogleBookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGoogleBookApi(): GoogleBookApi {

        return Retrofit.Builder()
            .baseUrl(GoogleBookApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBookApi::class.java)
    }
}
