package com.hussein.jetnotes.presentation.edit_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.NoteRepository
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(val repository: NoteRepository) : ViewModel() {

    val state = MutableStateFlow(EditScreenState())
    var originalNote: Note? = null

    fun onAction(action: EditScreenActions) {
        when (action) {
            is EditScreenActions.UpdateContent -> updateContent(action.content)
            is EditScreenActions.UpdateTitle -> updateTitle(action.title)
            is EditScreenActions.SaveNote -> saveNote()
            is EditScreenActions.LoadNote -> loadNote(action.id)
            EditScreenActions.ClearState -> clearState()
            is EditScreenActions.SelectCategory -> onCategorySelected(action.categoryId)
            EditScreenActions.LoadCategories -> loadCategories()
            is EditScreenActions.AddNewCategory -> addNewCategory(action.categoryName)
        }
    }

    fun saveNote() {
        if (isContentTheSame()) return

        viewModelScope.launch {
            repository.insertNote(
                note = Note(
                    id = originalNote?.id ?: 0,
                    categoryId = state.value.categoryId,
                    title = state.value.title,
                    content = state.value.content
                )
            )
        }

    }

    private fun loadNote(id: Int) {
        state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val note = repository.getNoteById(id)
            note?.let {
                state.update { old ->
                    old.copy(
                        title = note.title,
                        content = note.content,
                        categoryId = note.categoryId,
                        isLoading = false
                    )
                }
                originalNote = note
            }

        }

    }

    private fun addNewCategory(categoryName: String) {
        viewModelScope.launch {
            val category = Category(name = categoryName)
            repository.insertCategory(category = category)
            onCategorySelected(category.id)
        }
    }

    private fun loadCategories() {
        repository.getCategories().onEach { categories ->
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
        return state.value.title == originalNote?.title
                && state.value.content == originalNote?.content &&
                state.value.categoryId == originalNote?.categoryId
    }


    private fun clearState() {
        state.update { EditScreenState() }
    }


    private fun updateTitle(title: String) {
        state.update { old -> old.copy(title = title) }
    }

    private fun updateContent(content: String) {
        state.update { old -> old.copy(content = content) }
    }


    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return EditViewModel(container.noteRepository) as T
            }
        }
    }
}




