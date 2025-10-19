package com.hussein.jetnotes.presentation.main_app_destinations.main_screen.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.BorderStroke
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
import com.hussein.jetnotes.utils.DateTimeUtils
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListItem(
    modifier: Modifier = Modifier,
    note: Note,
    onItemClicked: (Int) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),

    ) {

    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onClick = {
            onItemClicked(note.id)
        }, border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
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
//                Text(
//                    text = note.content,
//                    style = MaterialTheme.typography.bodyLarge,
//                    maxLines = 5,
//                    overflow = TextOverflow.Ellipsis
//                )
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

//@Preview
//@Composable
//private fun NoteListItemPrev() {
//
//    val note = remember {
//        Note(
//            title = "Title",
//            content = LoremIpsum(100).values.joinToString { it },
//            categoryId = 1
//        )
//    }
//    NoteListItem(note = note, onItemClicked = {})
//}
