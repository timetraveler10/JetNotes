package com.hussein.jetnotes.presentation.secret_notes

import com.hussein.jetnotes.domain.models.Notable
import com.hussein.jetnotes.presentation.util.NoteListState

data class SecretNotesScreenState(
    // State for the passcode setup/input process
    val currentInput: String = "",
    val firstPasscodeAttempt: String? = null,
    val passcodeErrorMessage: String? = null,
    val isAuthSuccessful: Boolean = false, // True when passcode is correct

    // State for the secret notes list
    override val notes: List<Notable> = emptyList(),
    override val isLoading: Boolean = true,

    // Meta-state for the whole flow
    val isPasscodeSet: Boolean = false // Determines if we show setup or input
    ): NoteListState {
    // Helper to determine which setup step we are on
    val isConfirmationStep: Boolean
        get() = firstPasscodeAttempt != null
}