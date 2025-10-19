package com.hussein.jetnotes.presentation.main_app_destinations.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.models.Category
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.data.repository.NoteRepository
import com.hussein.jetnotes.presentation.main_app_destinations.util.SnackBarEvent
import com.hussein.jetnotes.presentation.main_app_destinations.util.SnackbarAction
import com.hussein.jetnotes.presentation.main_app_destinations.util.SnackbarController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(val repository: NoteRepository) : ViewModel() {

    val state = MutableStateFlow(MainScreenState())
    private val uiEvent = MutableSharedFlow<MainScreenUiEvent>()
    var deletedNote:Note? = null

    private var currentPage = 0
    private var isFetching = false
    private var allNotesLoaded = false
    private val pageSize = 20



    fun onAction(action: MainScreenActions) {
        when (action) {
            is MainScreenActions.LoadData -> loadData(action.categoryId)
            is MainScreenActions.SelectCategory -> onCategorySelect(action.categoryId)
            is MainScreenActions.AddNewCategory -> addNewCategory(action.categoryName)
            is MainScreenActions.DeleteNote -> deleteNote(action.note)
            MainScreenActions.LoadCategories -> loadCategories()
            is MainScreenActions.SearchNotes -> TODO()
        }
    }
    fun showSnackbar() {
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
    fun loadNextPage(categoryId: Int) {
        if (isFetching || allNotesLoaded) return

        viewModelScope.launch {
            isFetching = true

            val offset = currentPage * pageSize
            val newNotes = repository.getPaginatedNotes(categoryId, pageSize, offset)

            if (newNotes.isNotEmpty()) {
                val newList = state.value.notes + newNotes
                state.update { old -> old.copy(notes =  newList) } // Append the new notes to the existing list
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

    private fun addNewCategory(categoryName: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(name = categoryName))
        }
    }

    private fun deleteNote(note: Note) {
        deletedNote = note
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    private fun loadCategories() {
        if (state.value.categories.isEmpty()) {
            viewModelScope.launch {
                repository.getCategories().collect { categories ->
                    state.update { it.copy(categories = categories) }
                }
            }

        }
    }

    private fun loadData(categoryId: Int?) {

        state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val notesFlow = if (categoryId == null)
                repository.getAllNotes()
            else repository.getNotesByCategory(categoryId)

            notesFlow.catch { error ->
                state.update { it.copy(hasError = error.message) }
            }.collect { notes ->
                state.update {
                    Log.d("MainViewModel", state.value.toString())
                    it.copy(notes = notes, isLoading = false)
                }

            }

        }


    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return MainViewModel(container.noteRepository) as T
            }
        }
    }
}

