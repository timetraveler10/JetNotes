package com.hussein.jetnotes.data

import com.hussein.jetnotes.data.database.CategoryDao
import com.hussein.jetnotes.data.database.NoteDao
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note

class NoteRepository(val noteDao: NoteDao, val categoryDao: CategoryDao) {

    // yeah this repo is not necessary at all, but we keeping it future proof

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    fun getAllNotes() = noteDao.getAllNotes()

    fun getNotesByCategory(categoryId: Int) = noteDao.getNotesByCategory(categoryId)

    fun searchNotes(query: String) = noteDao.searchNotes(query)

    suspend fun getNoteById(id: Int) = noteDao.getNoteById(id)

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    fun getCategories() = categoryDao.getAllCategories()

}



