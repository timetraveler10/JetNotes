package com.hussein.jetnotes.data

import android.content.Context
import androidx.room.Room
import com.hussein.jetnotes.data.database.AppDatabase
import com.hussein.jetnotes.data.preferences.AppPreferences
import com.hussein.jetnotes.data.preferences.SecurityPreferences
import com.hussein.jetnotes.data.repository.CategoryRepository
import com.hussein.jetnotes.data.repository.NoteRepository
import com.hussein.jetnotes.data.repository.SecretNotesRepository

class AppContainer(val context: Context) {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context = context.applicationContext,
            klass = AppDatabase::class.java,
            name = "app_database"
        ).build()
    }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val secretNotesRepository by lazy { SecretNotesRepository(database.secretNoteDao()) }

    val securityPreferences by lazy { SecurityPreferences(context = context) }
    val appPreferences by lazy { AppPreferences(context = context) }
}