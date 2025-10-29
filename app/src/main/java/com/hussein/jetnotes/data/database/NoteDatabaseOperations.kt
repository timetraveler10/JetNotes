package com.hussein.jetnotes.data.database

import com.hussein.jetnotes.domain.models.Notable
import kotlinx.coroutines.flow.Flow

interface NoteDatabaseOperations {
    suspend fun insertNote(note: Notable)
    suspend fun deleteNote(note: Notable)
    fun getAllNotes(): Flow<List<Notable>>
    fun searchNotes(query: String): Flow<List<Notable>>
    suspend fun getNoteById(id: Int): Notable?

}