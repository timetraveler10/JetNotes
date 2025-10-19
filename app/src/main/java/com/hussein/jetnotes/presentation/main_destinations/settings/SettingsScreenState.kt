package com.hussein.jetnotes.presentation.main_app_destinations.settings

import com.hussein.jetnotes.presentation.main_app_destinations.util.ThemeStates

data class SettingsScreenState(
    val currentTheme: ThemeStates = ThemeStates.SYSTEM_DEFAULT,
    val isDynamicColorEnabled: Boolean = true,
    val isLoading: Boolean = false
)