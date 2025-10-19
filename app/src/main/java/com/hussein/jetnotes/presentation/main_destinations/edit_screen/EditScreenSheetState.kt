package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen

interface EditScreenSheetState {
    data object Hidden : EditScreenSheetState
    data object AddCategory : EditScreenSheetState
    data object Formatting : EditScreenSheetState
}