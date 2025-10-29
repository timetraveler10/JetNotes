package com.hussein.jetnotes.presentation.main_destinations.notes

import com.hussein.jetnotes.data.models.Note

sealed interface NotesScreenActions {
    data class LoadData(val categoryId: Int?) : NotesScreenActions
    data class SearchNotes(val query: String) : NotesScreenActions
    data class SelectCategory(val categoryId: Int?) : NotesScreenActions
    data class AddNewCategory(val categoryName: String) : NotesScreenActions
    data class DeleteNote(val note: Note) : NotesScreenActions
    data object LoadCategories : NotesScreenActions
    data class OnToggleNoteSelection(val note: Note) : NotesScreenActions
    data object OnToggleSelectionMode : NotesScreenActions
    data object OnBulkDelete : NotesScreenActions
}
