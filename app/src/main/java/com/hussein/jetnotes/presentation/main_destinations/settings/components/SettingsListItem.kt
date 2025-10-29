package com.hussein.jetnotes.presentation.main_destinations.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R

@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    title: String,
    settingDescription: String,
    onClick: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    color: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit = {},
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        shape = shape,
        color = color,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            leadingIcon()
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    maxLines = 3,
                    text = settingDescription,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            trailingIcon()
        }
    }
}

@Preview
@Composable
private fun SettingsListItemPrev() {
    SettingsListItem(
        title = "Dark theme",
        settingDescription = "Description", leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.checkmark),
                contentDescription = null
            )
        }, onClick = {}
    )
}

