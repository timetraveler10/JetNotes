package com.hussein.jetnotes.presentation.main_destinations.notes

import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.presentation.util.NoteListState

data class NotesScreenState(
    override val notes: List<Note> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    override val isLoading: Boolean = false,
    val page: Int = 1,
    val listScrollPosition: Int = 0,
    val canPaginate: Boolean = true,
    val selectedNotes: List<Note> = emptyList(),
    val isSelectionModeEnabled: Boolean = false
): NoteListState
