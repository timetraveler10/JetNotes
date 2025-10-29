package com.hussein.jetnotes.presentation.secret_notes

sealed interface SecretNoteActions {
    data object LoadNextPage : SecretNoteActions
    data class OnPasscodeInputChange(val value: String) : SecretNoteActions
    data object HandlePasscodeSetupSubmit : SecretNoteActions
    data object CheckIfPasscodeExists : SecretNoteActions
    data class HandlePasscodeEntrySubmit(val rawPassword:String) : SecretNoteActions

}