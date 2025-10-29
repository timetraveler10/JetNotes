package com.hussein.jetnotes.presentation.main_destinations.edit

import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlListOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlParagraphOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlTextSpanOptions

sealed interface EditScreenActions {
    data class UpdateTitle(val title: String) : EditScreenActions
    data class UpdateContent(val content: String) : EditScreenActions
    data object SaveNote : EditScreenActions
    data class LoadNote(val id: Int) : EditScreenActions
    data class SelectCategory(val categoryId: Int?) : EditScreenActions
    data object LoadCategories : EditScreenActions
    data class AddNewCategory(val categoryName: String) : EditScreenActions
    data class ToggleMarkDownSpanOption(val option: HtmlTextSpanOptions) : EditScreenActions
    data class ToggleMarkDownParagraphOption(val option: HtmlParagraphOptions) : EditScreenActions
    data class ToggleListOption(val option: HtmlListOptions): EditScreenActions

}