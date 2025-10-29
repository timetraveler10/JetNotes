package com.hussein.jetnotes.presentation.secret_notes.notes

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.main_destinations.notes.components.NoteListItem
import com.hussein.jetnotes.presentation.secret_notes.SecretNoteActions
import com.hussein.jetnotes.presentation.util.NoteListState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SecretNotes(
    state: NoteListState,
    onNavigateToEdit: (Int?) -> Unit,
    onAction: (SecretNoteActions) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val lazyListState = rememberLazyListState()

        SecretNotesScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = state,
            lazyColumnState = lazyListState,
            onNavigateToEdit = onNavigateToEdit,
            animatedContentScope = animatedContentScope,
            sharedElementTransitionScope = sharedTransitionScope,
            onLoadNextPage = {
                onAction(SecretNoteActions.LoadNextPage)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SecretNotesScreenContent(
    modifier: Modifier = Modifier,
    state: NoteListState,
    lazyColumnState: LazyListState = rememberLazyListState(),
    onNavigateToEdit: (Int?) -> Unit,
    onLoadNextPage: () -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedElementTransitionScope: SharedTransitionScope
) {
    with(sharedElementTransitionScope) {

        LazyColumn(
            state = lazyColumnState,
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.notes, key = { note -> note.id }) { note ->
                NoteListItem(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("secret-note-${note.id}"),
                            animatedVisibilityScope = animatedContentScope,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize,
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(12.dp)
                            )
                        ).animateItem(),
                    note = note,
                    onItemClicked = { id -> onNavigateToEdit(id) },
                    sharedElementTransitionScope = sharedElementTransitionScope,
                    animatedContentScope = animatedContentScope,
                    shape = TODO(),
                    onNoteSelect = TODO(),
                    onTriggerSelection = TODO(),
                    isSelectionEnabled = TODO(),
                    isSelected = TODO(),
                )
            }

        }
    }
}


