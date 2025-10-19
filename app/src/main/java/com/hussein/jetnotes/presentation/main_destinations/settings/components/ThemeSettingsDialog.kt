package com.hussein.jetnotes.presentation.main_app_destinations.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hussein.jetnotes.presentation.main_app_destinations.util.ThemeStates

@Composable
fun ThemeSettingsDialog(
    onToggle: (ThemeStates) -> Unit,
    currentTheme: ThemeStates,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(windowTitle = "Dark Theme Settings")
    ) {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeStates.entries.forEach { theme: ThemeStates ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = true, onClick = { onToggle(theme) }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(
                        maxLines = 1,
                        text = theme.displayName,
                        style = MaterialTheme.typography.titleSmall
                    )
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = {
                            onToggle(theme)
                        })
                }
            }

        }
    }
}