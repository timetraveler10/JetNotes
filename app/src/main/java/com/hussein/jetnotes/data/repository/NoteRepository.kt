package com.hussein.jetnotes.data.repository

import com.hussein.jetnotes.data.database.NoteDao
import com.hussein.jetnotes.data.database.NoteDatabaseOperations
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.domain.models.Notable

class NoteRepository(val noteDao: NoteDao) : NoteDatabaseOperations {

    suspend fun getPaginatedNotes(categoryId: Int, limit: Int, offset: Int): List<Note> =
        noteDao.getNotesByCategoryPaginated(categoryId = categoryId, offset = offset, limit = limit)

    fun getNotesByCategory(categoryId: Int) = noteDao.getNotesByCategory(categoryId)

    override suspend fun insertNote(note: Notable) = noteDao.insertNote(note as Note)

    override suspend fun deleteNote(note: Notable) = noteDao.deleteNote(note as Note)

    override suspend fun getNoteById(id: Int) = noteDao.getNoteById(id)

    override fun getAllNotes() = noteDao.getAllNotes()

    override fun searchNotes(query: String) = noteDao.searchNotes(query)

   suspend fun bulkDelete(notes: List<Note>) = noteDao.bulkDeleteNotes(notes)




}