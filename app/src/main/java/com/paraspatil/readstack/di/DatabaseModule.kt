package com.paraspatil.readstack.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE DATABASE IF NOT EXIST search_results(
            id TEXT PRIMARY KEY NOT NULL,
            title TEXT NOT NULL,
            author TEXT NOT NULL,
            thumbnailUrl TEXT NOT NULL,
            description TEXT ,
            pageCount INTEGER ,
            publishedDate TEXT ,
            searchQuery TEXT NOT NULL,
            timestamp INTEGER NOT NULL
                )
            """.trimIndent()
            )
        }
}

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BookDatabase{
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "readstack_db"
        )
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()

    }
    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao{
        return database.dao

    }
}