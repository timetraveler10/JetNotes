package com.hussein.jetnotes.presentation.main_app_destinations.main_screen

import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note

data class MainScreenState(
    val notes: List<Note> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val isLoading: Boolean = false,
    val hasError: String? = null,
    val page: Int = 1,
    val listScrollPosition: Int = 0,
    val canPaginate: Boolean = true,
)
