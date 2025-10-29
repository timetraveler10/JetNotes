package com.hussein.jetnotes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hussein.jetnotes.presentation.main_destinations.util.ThemeStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppPreferences(val context: Context) {
    companion object {
        val DARK_THEME_PREF_KEY = intPreferencesKey("dark_theme_pref")
        val DYN_COLOR_PREF_KEY = booleanPreferencesKey("dynamic_color_pref")
    }


    suspend fun saveDarkThemePreference(themeStates: ThemeStates) {
        context.dataStore.edit { settings ->
            settings[DARK_THEME_PREF_KEY] = themeStates.ordinal
        }
    }

    suspend fun saveDynamicThemePreference(enabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[DYN_COLOR_PREF_KEY] = enabled
        }
    }

    val getDarkModePreference: Flow<ThemeStates> = context.dataStore.data.map { preferences ->
        val ordinal = preferences[DARK_THEME_PREF_KEY] ?: ThemeStates.SYSTEM_DEFAULT.ordinal
        ThemeStates.entries[ordinal]
    }

    val getDynamicColorPreference: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DYN_COLOR_PREF_KEY] ?: true
    }
}