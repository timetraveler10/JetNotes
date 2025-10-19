package com.hussein.jetnotes.presentation.main_app_destinations.settings.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussein.jetnotes.presentation.components.NavigateBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    MediumTopAppBar(
        title = {
            Text("Settings")
        },
        navigationIcon = {
            NavigateBackButton(onNavigateBack = onNavigateBack)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh))
}
