package com.hussein.jetnotes.presentation.secret_notes_screen

import androidx.lifecycle.ViewModel
import com.hussein.jetnotes.data.NoteRepository
import com.hussein.jetnotes.data.security.CipherHelper

class SecretNotesViewModel(
    val repository: NoteRepository,
    val cipherHelper: CipherHelper
) : ViewModel() {

}