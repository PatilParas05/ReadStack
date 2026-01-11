package com.paraspatil.readstack.di

import android.content.Context
import androidx.room.Room
import com.paraspatil.readstack.data.local.BookDao
import com.paraspatil.readstack.data.local.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BookDatabase{
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "readstack_db"
        ).build()

    }
    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao{
        return database.dao

    }
}