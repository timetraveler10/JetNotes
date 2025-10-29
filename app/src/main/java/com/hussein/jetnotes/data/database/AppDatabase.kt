package com.hussein.jetnotes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.data.models.SecretNote

@Database(entities = [Note::class, Category::class, SecretNote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
    abstract fun secretNoteDao(): SecretNotesDao
}
