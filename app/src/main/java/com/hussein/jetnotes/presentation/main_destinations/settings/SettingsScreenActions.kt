package com.hussein.jetnotes.presentation.main_app_destinations.settings

import com.hussein.jetnotes.presentation.main_app_destinations.util.ThemeStates

interface SettingsScreenActions {
    data class ToggleDarkTheme(val themeStates: ThemeStates) : SettingsScreenActions
    data class ToggleDynamicColorTheme(val enabled: Boolean) : SettingsScreenActions


}