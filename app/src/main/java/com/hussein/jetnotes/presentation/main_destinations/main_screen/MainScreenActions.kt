package com.hussein.jetnotes.presentation.main_app_destinations.main_screen

import com.hussein.jetnotes.data.models.Note

sealed interface MainScreenActions {
    data class LoadData(val categoryId: Int?) :MainScreenActions
    data class SearchNotes(val query: String) : MainScreenActions
    data class SelectCategory(val categoryId: Int?) : MainScreenActions
    data class AddNewCategory(val categoryName: String) : MainScreenActions
    data class DeleteNote(val note: Note) : MainScreenActions
    data object LoadCategories : MainScreenActions
}
