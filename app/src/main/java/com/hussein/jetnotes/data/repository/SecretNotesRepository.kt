package com.hussein.jetnotes.data.repository

import com.hussein.jetnotes.data.database.NoteDatabaseOperations
import com.hussein.jetnotes.data.database.SecretNotesDao
import com.hussein.jetnotes.data.models.SecretNote
import com.hussein.jetnotes.domain.models.Notable
import kotlinx.coroutines.flow.Flow

class SecretNotesRepository(private val secretNotesDao: SecretNotesDao) : NoteDatabaseOperations {

    suspend fun getPaginatedSecretNotes(limit: Int, offset: Int): List<SecretNote> =
        secretNotesDao.getNotesPaginated(offset = offset, limit = limit)

    override suspend fun insertNote(note: Notable) = secretNotesDao.upsert(note as SecretNote)

    override suspend fun deleteNote(note: Notable) = secretNotesDao.delete(note as SecretNote)

    override suspend fun getNoteById(id: Int) = secretNotesDao.getById(id)

    override fun getAllNotes() = secretNotesDao.getAll()

    override fun searchNotes(query: String): Flow<List<Notable>> {
        TODO("Not yet implemented")
    }

}