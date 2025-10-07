package com.hussein.jetnotes.data

import android.content.Context
import androidx.room.Room
import com.hussein.jetnotes.data.database.AppDatabase

class AppContainer(val context: Context) {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context = context.applicationContext,
            klass = AppDatabase::class.java,
            name = "app_database"
        ).build()
    }
    val noteRepository by lazy { NoteRepository(database.noteDao(), database.categoryDao()) }
}