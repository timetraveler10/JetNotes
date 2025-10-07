package com.hussein.jetnotes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note

@Database(entities = [Note::class, Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
}
