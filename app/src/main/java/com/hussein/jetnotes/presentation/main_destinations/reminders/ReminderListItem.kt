package com.hussein.jetnotes.presentation.main_destinations.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.data.models.Reminder
import com.hussein.jetnotes.ui.theme.ListComponentsShapes
import com.hussein.jetnotes.utils.DateTimeUtils

@Composable
fun ReminderListItem(
    modifier: Modifier = Modifier,
    onCheck: (isChecked: Boolean) -> Unit,
    reminder: Reminder,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        shape = shape,
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = reminder.checked, onCheckedChange = onCheck)
                Text(
                    text = reminder.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = if (reminder.checked) MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.LineThrough
                    ) else MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Added at ${DateTimeUtils.formatTimeStamp(reminder.timeStamp)}",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Preview
@Composable
private fun ReminderListItemPrev() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        ReminderListItem(
            shape = ListComponentsShapes.TopItem,
            onCheck = {}, reminder = Reminder(
                id = 1,
                title = "Some important thing",
                checked = true,
                timeStamp = System.currentTimeMillis(),
                remindAt = System.currentTimeMillis()
            ), onClick = {}
        )
        ReminderListItem(
            shape = ListComponentsShapes.MidItem,
            onCheck = {}, reminder = Reminder(
                id = 1,
                title = "Some important thing",
                checked = false,
                timeStamp = System.currentTimeMillis(),
                remindAt = System.currentTimeMillis()
            ), onClick = {}
        )
        ReminderListItem(
            shape = ListComponentsShapes.BottomItem,
            onCheck = {}, reminder = Reminder(
                id = 1,
                title = "Some important thing",
                checked = true,
                timeStamp = System.currentTimeMillis(),
                remindAt = System.currentTimeMillis()
            ), onClick = {}
        )
    }
}