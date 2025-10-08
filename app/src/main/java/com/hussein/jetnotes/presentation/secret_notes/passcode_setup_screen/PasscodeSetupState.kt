package com.hussein.jetnotes.presentation.passcode_setup_screen

data class PasscodeSetupState(
    // The current value in the text field
    val currentInput: String = "",
    // The passcode from the first step, stored for confirmation
    val firstPasscodeAttempt: String? = null,
    // A message to show the user (e.g., "Passcodes don't match")
    val errorMessage: String? = null,
    // A flag to signal that the passcode was saved successfully
    val isPasscodeSaved: Boolean = false
) {
    // Helper to determine which step we are on, for UI text like "Enter Passcode" or "Confirm Passcode"
    val isConfirmationStep: Boolean
        get() = firstPasscodeAttempt != null
}