package com.hussein.jetnotes.presentation.edit_screen

import com.hussein.jetnotes.presentation.main_screen.MainScreenActions

sealed interface EditScreenActions {
    data class UpdateTitle(val title: String) : EditScreenActions
    data class UpdateContent(val content: String) : EditScreenActions
    data object SaveNote : EditScreenActions
    data class LoadNote(val id: Int) : EditScreenActions
    object ClearState : EditScreenActions
    data class SelectCategory(val categoryId: Int?) : EditScreenActions
    data object LoadCategories : EditScreenActions
    data class AddNewCategory(val categoryName: String) : EditScreenActions

}