package com.hussein.jetnotes.presentation.main_destinations.notes.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.domain.models.Notable
import com.hussein.jetnotes.utils.DateTimeUtils
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListItem(
    modifier: Modifier = Modifier,
    note: Notable,
    onItemClicked: (Int) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    sharedElementTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onNoteSelect: (Note) -> Unit = {},
    onTriggerSelection: () -> Unit,
    isSelectionEnabled: Boolean,
    isSelected: Boolean
) {

    with(sharedElementTransitionScope) {
        Surface(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState("note-${note.id}"),
                    animatedVisibilityScope = animatedContentScope,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(12.dp))
                )
                .combinedClickable(
                    enabled = true,
                    onClick = { if (isSelectionEnabled) onNoteSelect(note as Note) else onItemClicked(note.id) },
                    onLongClick = { onTriggerSelection()
                    onNoteSelect(note as Note)
                    })
                .then(modifier),
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(
                width = if (isSelected && isSelectionEnabled) 2.dp else 1.dp,
                color = if (isSelected && isSelectionEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {

                if (note.title.isNotEmpty()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                if (note.content.isNotEmpty()) {
                    val state = rememberRichTextState()
                    LaunchedEffect(note.content) {
                        state.setHtml(note.content)
                    }
                    RichText(
                        state = state,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = DateTimeUtils.formatTimeStamp(note.date),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }

}
