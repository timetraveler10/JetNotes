package com.hussein.jetnotes.presentation.main_app_destinations.main_screen

interface MainScreenUiEvent {
    data class ShowSnackBar(val message: String) : MainScreenUiEvent
}