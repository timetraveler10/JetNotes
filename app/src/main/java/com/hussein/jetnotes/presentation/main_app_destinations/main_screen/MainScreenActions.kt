package com.hussein.jetnotes.presentation.main_screen

sealed interface MainScreenActions {
    data class PinNote(val noteId: Int) : MainScreenActions
    data class LoadData(val categoryId: Int?) :MainScreenActions
    data class SearchNotes(val query: String) : MainScreenActions
    data class SelectCategory(val categoryId: Int?) : MainScreenActions
    data class AddNewCategory(val categoryName: String) : MainScreenActions
    data class DeleteNote(val noteId: Int) : MainScreenActions
    data object LoadCategories : MainScreenActions
}
