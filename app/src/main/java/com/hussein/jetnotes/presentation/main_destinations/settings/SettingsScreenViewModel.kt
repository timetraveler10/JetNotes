package com.hussein.jetnotes.presentation.main_app_destinations.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.preferences.AppPreferences
import com.hussein.jetnotes.presentation.main_app_destinations.util.ThemeStates
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenViewModel(val appPreferences: AppPreferences) : ViewModel() {

    val state = combine(
        flow = appPreferences.getDarkModePreference,
        flow2 = appPreferences.getDynamicColorPreference
    ) { currentTheme , dynamicColorEnabled ->
        SettingsScreenState(
            currentTheme = currentTheme,
            isDynamicColorEnabled = dynamicColorEnabled,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = SettingsScreenState(isLoading = true),
        started = SharingStarted.WhileSubscribed(5000)
    )


    fun onAction(action: SettingsScreenActions) {
        when (action) {
            is SettingsScreenActions.ToggleDarkTheme -> onThemeChange(action.themeStates)
            is SettingsScreenActions.ToggleDynamicColorTheme -> onDynamicColorChange(action.enabled)

        }
    }

    fun onDynamicColorChange(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.saveDynamicThemePreference(enabled)
        }
    }

    fun onThemeChange(themeStates: ThemeStates) {
        viewModelScope.launch {
            appPreferences.saveDarkThemePreference(themeStates)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return SettingsScreenViewModel(container.appPreferences) as T
            }
        }
    }

}