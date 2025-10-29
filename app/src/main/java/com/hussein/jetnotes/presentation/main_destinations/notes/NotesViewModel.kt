package com.hussein.jetnotes.presentation.main_destinations.notes

import android.util.Log
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
import com.hussein.jetnotes.presentation.main_destinations.util.SnackBarEvent
import com.hussein.jetnotes.presentation.main_destinations.util.SnackbarAction
import com.hussein.jetnotes.presentation.main_destinations.util.SnackbarController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    val repository: NoteRepository,
    val categoryRepository: CategoryRepository
) : ViewModel() {

    val state = MutableStateFlow(NotesScreenState())

    private var deletedNote: Note? = null

    private var currentPage = 0
    private var isFetching = false
    private var allNotesLoaded = false
    private val pageSize = 20


    fun onAction(action: NotesScreenActions) {
        when (action) {
            is NotesScreenActions.LoadData -> onLoadData(action.categoryId)
            is NotesScreenActions.SelectCategory -> onCategorySelect(action.categoryId)
            is NotesScreenActions.AddNewCategory -> onAddNewCategory(action.categoryName)
            is NotesScreenActions.DeleteNote -> onDeleteNote(action.note)
            NotesScreenActions.LoadCategories -> onLoadCategories()
            is NotesScreenActions.SearchNotes -> TODO()
            is NotesScreenActions.OnToggleNoteSelection -> onToggleNoteSelection(action.note)
            NotesScreenActions.OnToggleSelectionMode -> onToggleSelectionMode()
            NotesScreenActions.OnBulkDelete -> onBulkDelete()
        }
    }

    private fun showSnackbar() {
        viewModelScope.launch {
            SnackbarController.sendEvent(
                event = SnackBarEvent(
                    message = "Note delete!",
                    action = SnackbarAction(
                        name = "Undo",
                        action = {}
                    )
                )
            )
        }
    }

    private fun onToggleNoteSelection(note: Note) {
        if (state.value.selectedNotes.contains(note)) {
            state.update { old -> old.copy(selectedNotes = old.selectedNotes - note) }
        } else state.update { old -> old.copy(selectedNotes = old.selectedNotes + note) }
    }

    private fun onToggleSelectionMode() {
        val enabled = state.value.isSelectionModeEnabled
        if (enabled) {
            state.update { it.copy(isSelectionModeEnabled = false, selectedNotes = emptyList()) }
        } else {
            state.update { it.copy(isSelectionModeEnabled = true) }
        }
    }

    private fun onBulkDelete() {
        viewModelScope.launch {
            repository.bulkDelete(state.value.selectedNotes)
        }
        onToggleSelectionMode()
    }

    private fun onLoadNextPage(categoryId: Int) {
        if (isFetching || allNotesLoaded) return

        viewModelScope.launch {
            isFetching = true

            val offset = currentPage * pageSize
            val newNotes = repository.getPaginatedNotes(categoryId, pageSize, offset)

            if (newNotes.isNotEmpty()) {
                val newList = state.value.notes + newNotes
                state.update { old -> old.copy(notes = newList) } // Append the new notes to the existing list
                currentPage++
            } else {
                // If the query returns an empty list, we've loaded all notes.
                allNotesLoaded = true
            }

            isFetching = false
        }
    }

    private fun onCategorySelect(categoryId: Int?) {
        state.update { it.copy(selectedCategoryId = categoryId) }
    }

    private fun onAddNewCategory(categoryName: String) {
        viewModelScope.launch {
            categoryRepository.insertCategory(Category(name = categoryName))
        }
    }

    private fun onDeleteNote(note: Note) {
        deletedNote = note
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    private fun onLoadCategories() {
        if (state.value.categories.isEmpty()) {
            viewModelScope.launch {
                categoryRepository.getCategories().collect { categories ->
                    state.update { it.copy(categories = categories) }
                }
            }

        }
    }

    private fun onLoadData(categoryId: Int?) {

        state.update { it.copy(isLoading = true) }

        val notesFlow = if (categoryId == null)
            repository.getAllNotes()
        else repository.getNotesByCategory(categoryId)

        notesFlow.catch { error ->
            //Todo : add proper error handling
        }.onEach { notes ->
            state.update {
                Log.d("MainViewModel", state.value.toString())
                it.copy(notes = notes, isLoading = false)
            }

        }.launchIn(viewModelScope)

    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return NotesViewModel(
                    repository = container.noteRepository,
                    categoryRepository = container.categoryRepository
                ) as T
            }
        }
    }
}

