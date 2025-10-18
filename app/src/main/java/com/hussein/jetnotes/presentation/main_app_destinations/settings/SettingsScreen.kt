package com.hussein.jetnotes.presentation.main_app_destinations.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.main_app_destinations.settings.components.SettingsScreenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen() {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showThemeSettingsDialog = remember { mutableListOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        SettingsScreenTopAppBar(
            onNavigateBack = {},
            scrollBehavior = scrollBehavior
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), horizontalAlignment = Alignment.Start
        ) {

            Text(text = "Display", style = MaterialTheme.typography.labelMedium)

        }

    }

}