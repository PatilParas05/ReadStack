package com.paraspatil.readstack.di

import android.app.Application
import androidx.room.Room
import com.paraspatil.readstack.data.local.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {
    @Provides
    @Singleton
    fun provideBookDatabase(app: Application): BookDatabase {
        return Retrofit.Builder()
            .baseUrl(BookDatabase.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookDatabase::class.java)
    }
}