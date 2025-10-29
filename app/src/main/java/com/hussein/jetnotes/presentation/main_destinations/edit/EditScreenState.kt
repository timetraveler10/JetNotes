package com.hussein.jetnotes.presentation.main_destinations.edit

import com.hussein.jetnotes.data.models.Category
import com.mohamedrejeb.richeditor.model.RichTextState

data class EditScreenState(
    val title: String = "",
    val content: RichTextState = RichTextState(),
    val categoryId: Int? = null,
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
)
