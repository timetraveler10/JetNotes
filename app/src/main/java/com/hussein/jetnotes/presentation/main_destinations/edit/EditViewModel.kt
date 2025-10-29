package com.hussein.jetnotes.presentation.main_destinations.edit

import android.util.Log
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.data.repository.CategoryRepository
import com.hussein.jetnotes.data.repository.NoteRepository
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlListOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlParagraphOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlTextSpanOptions
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(val repository: NoteRepository , val categoryRepository: CategoryRepository) : ViewModel() {

    val state = MutableStateFlow(EditScreenState())
    var originalNote: Note? = null
    val TAG = "EditViewModel"
    fun onAction(action: EditScreenActions) {
        when (action) {

            is EditScreenActions.UpdateContent -> onContentChange(action.content)
            is EditScreenActions.UpdateTitle -> onTitleChange(action.title)
            is EditScreenActions.SaveNote -> onNoteSave()
            is EditScreenActions.LoadNote -> onLoadNote(action.id)
            is EditScreenActions.SelectCategory -> onCategorySelected(action.categoryId)
            is EditScreenActions.AddNewCategory -> onInsertNewCategory(action.categoryName)
            EditScreenActions.LoadCategories -> onLoadCategories()
            is EditScreenActions.ToggleMarkDownSpanOption -> toggleTextFormatOption(action.option)
            is EditScreenActions.ToggleMarkDownParagraphOption -> addParagraphAlignmentOption(option = action.option)
            is EditScreenActions.ToggleListOption -> toggleListOption(action.option)
        }
    }


    private fun toggleTextFormatOption(option: HtmlTextSpanOptions) {
        when (option) {

            HtmlTextSpanOptions.BOLD -> toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            HtmlTextSpanOptions.ITALIC -> toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            HtmlTextSpanOptions.UNDERLINE -> toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            HtmlTextSpanOptions.LINE_THROUGH -> toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        }
    }

    private fun addParagraphAlignmentOption(option: HtmlParagraphOptions){
        when(option){
            HtmlParagraphOptions.AlignLeft -> addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left))
            HtmlParagraphOptions.AlignCenter -> addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
            HtmlParagraphOptions.AlignRight -> addParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right))
        }
    }

    private fun toggleListOption(htmlListOptions: HtmlListOptions){
        when(htmlListOptions){
            HtmlListOptions.Ordered -> state.value.content.toggleOrderedList()
            HtmlListOptions.Unordered -> state.value.content.toggleUnorderedList()
        }
    }

    private fun toggleSpanStyle(spanStyle: SpanStyle) {
        state.value.content.toggleSpanStyle(spanStyle = spanStyle)
    }

    private fun addParagraphStyle(paragraphStyle: ParagraphStyle) {
        state.value.content.addParagraphStyle(paragraphStyle)
    }

    private fun onNoteSave() {
        if (isContentTheSame()) return // if content is the same, don't save

        if (state.value.title.isNotEmpty() || state.value.content.annotatedString.isNotEmpty()) { // at least a title or content is required
            viewModelScope.launch {
                repository.insertNote(
                    note = Note(
                        id = originalNote?.id ?: 0,
                        categoryId = state.value.categoryId,
                        title = state.value.title,
                        content = state.value.content.toHtml()
                    )
                )

            }
            Log.d(TAG, "saveNote: note inserted")
        }

    }

    private fun onLoadNote(id: Int) {
        state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val note = repository.getNoteById(id)
            note?.let {
                state.update { old ->
                    old.copy(
                        title = note.title,
                        content = RichTextState().apply { setHtml(note.content) },
                        categoryId = note.categoryId,
                        isLoading = false
                    )
                }
                originalNote = note
            }

        }

    }

    private fun onInsertNewCategory(categoryName: String) {
        viewModelScope.launch {
            val category = Category(name = categoryName)
            categoryRepository.insertCategory(category = category)
            onCategorySelected(category.id)
        }
    }

    private fun onLoadCategories() {
        categoryRepository.getCategories().onEach { categories ->
            state.update { old ->
                old.copy(categories = categories)
            }
        }.launchIn(viewModelScope)
    }

    private fun onCategorySelected(categoryId: Int?) {
        state.update { old ->
            old.copy(categoryId = categoryId)
        }
    }

    private fun isContentTheSame(): Boolean {
        val currentContentHtml = state.value.content.toHtml()

        return state.value.title == originalNote?.title
                && currentContentHtml == originalNote?.content
                && state.value.categoryId == originalNote?.categoryId
    }

    private fun onTitleChange(title: String) {
        state.update { old -> old.copy(title = title) }
    }

    private fun onContentChange(content: String) {
        state.update { old -> old.copy(content = RichTextState().apply { setText(content) }) }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return EditViewModel(
                    repository = container.noteRepository,
                    categoryRepository = container.categoryRepository
                ) as T
            }
        }
    }
}




