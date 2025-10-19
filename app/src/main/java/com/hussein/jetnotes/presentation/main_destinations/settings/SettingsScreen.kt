package com.hussein.jetnotes.presentation.main_app_destinations.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R
import com.hussein.jetnotes.presentation.main_app_destinations.settings.components.SettingsListItem
import com.hussein.jetnotes.presentation.main_app_destinations.settings.components.SettingsScreenTopAppBar
import com.hussein.jetnotes.presentation.main_app_destinations.settings.components.ThemeSettingsDialog
import com.hussein.jetnotes.ui.theme.ListComponentsShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onAction: (SettingsScreenActions) -> Unit,
    onNavigateBack: () -> Unit
) {

    var showThemeSettingsDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsScreenTopAppBar(onNavigateBack = onNavigateBack)
        }
    ) { innerPadding ->

        if (showThemeSettingsDialog) {
            ThemeSettingsDialog(
                onToggle = { onAction(SettingsScreenActions.ToggleDarkTheme(it)) },
                currentTheme = state.currentTheme,
                onDismiss = { showThemeSettingsDialog = false }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(text = "Display", style = MaterialTheme.typography.titleMedium)
            SettingsListItem(
                shape = ListComponentsShapes.TopItem,
                title = "Dark theme",
                settingDescription = state.currentTheme.displayName,
                onClick = { showThemeSettingsDialog = true },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.dark_mode),
                        contentDescription = "dark mode settings"
                    )
                }
            )
            SettingsListItem(
                shape = ListComponentsShapes.BottomItem,
                title = "Dark theme",
                settingDescription = state.currentTheme.displayName,
                onClick = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.dyn_color),
                        contentDescription = "dark mode settings"
                    )
                } , trailingIcon = {
                    Switch(
                        checked = state.isDynamicColorEnabled,
                        onCheckedChange = {
                            onAction(SettingsScreenActions.ToggleDynamicColorTheme(it))
                        }
                    )
                }
            )
        }

    }

}