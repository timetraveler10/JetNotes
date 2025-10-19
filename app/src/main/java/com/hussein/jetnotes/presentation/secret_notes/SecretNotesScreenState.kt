package com.hussein.jetnotes.presentation.secret_notes 

import com.hussein.jetnotes.data.models.Note

data class SecretNotesScreenState(
    // State for the passcode setup/input process
    val currentInput: String = "",
    val firstPasscodeAttempt: String? = null,
    val passcodeErrorMessage: String? = null,
    val isAuthSuccessful: Boolean = false, // True when passcode is correct
