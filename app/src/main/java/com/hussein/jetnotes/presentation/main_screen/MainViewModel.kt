package com.hussein.jetnotes.presentation.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.NoteRepository
import com.hussein.jetnotes.data.models.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(val repository: NoteRepository) : ViewModel() {

    val state = MutableStateFlow(MainScreenState())


    fun onAction(action: MainScreenActions) {
        when (action) {
            is MainScreenActions.LoadData -> loadData(action.categoryId)


            is MainScreenActions.PinNote -> TODO()
            is MainScreenActions.SearchNotes -> TODO()
            is MainScreenActions.SelectCategory -> {
                state.update { it.copy(selectedCategoryId = action.categoryId) }
            }

            is MainScreenActions.AddNewCategory -> addNewCategory(action.categoryName)
            is MainScreenActions.DeleteNote -> TODO()
            MainScreenActions.LoadCategories -> loadCategories()
        }
    }

    private fun addNewCategory(categoryName: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(name = categoryName))
        }
    }

    private fun loadCategories(){
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

