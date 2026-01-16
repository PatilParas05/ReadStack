package com.paraspatil.readstack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class, SearchResultEntity::class],
    version = 3,
    exportSchema = false

)
abstract class BookDatabase : RoomDatabase() {
    abstract val dao: BookDao
}