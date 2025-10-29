package com.hussein.jetnotes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hussein.jetnotes.data.models.SecretNote
import kotlinx.coroutines.flow.Flow

@Dao
interface SecretNotesDao {

    @Query("SELECT * FROM SecretNote  ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getNotesPaginated(limit: Int, offset: Int): List<SecretNote>

    @Query("SELECT * FROM secretnote")
    fun getAll(): Flow<List<SecretNote>>

    @Query("SELECT * FROM secretnote WHERE id = :id")
    fun getById(id: Int): SecretNote

    @Upsert
    suspend fun upsert(note: SecretNote)

    @Delete
    suspend fun delete(note: SecretNote)


}