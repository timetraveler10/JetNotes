package com.hussein.jetnotes.presentation.util

import com.hussein.jetnotes.domain.models.Notable

interface NoteListState {
    val notes: List<Notable>
    val isLoading: Boolean
}