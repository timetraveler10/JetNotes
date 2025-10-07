package com.hussein.jetnotes.presentation.edit_screen

import com.hussein.jetnotes.data.models.Category

data class EditScreenState(
    val title: String = "",
    val content: String = "",
    val categoryId: Int? = null,
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList() ,
)
