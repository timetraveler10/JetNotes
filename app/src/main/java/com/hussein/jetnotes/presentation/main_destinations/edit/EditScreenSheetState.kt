package com.hussein.jetnotes.presentation.main_destinations.edit

interface EditScreenSheetState {
    data object Hidden : EditScreenSheetState
    data object AddCategory : EditScreenSheetState
    data object Formatting : EditScreenSheetState
}