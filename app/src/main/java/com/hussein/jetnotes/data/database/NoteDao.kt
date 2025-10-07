package com.hussein.jetnotes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.hussein.jetnotes.data.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {


    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Upsert
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM note WHERE categoryId = :categoryId")
    fun getNotesByCategory(categoryId: Int): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<Note>>





}