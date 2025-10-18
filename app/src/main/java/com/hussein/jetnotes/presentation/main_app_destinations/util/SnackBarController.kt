package com.hussein.jetnotes.presentation.main_app_destinations.util

data class SnackBarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val action: suspend () -> Unit
)