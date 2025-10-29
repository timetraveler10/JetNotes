package com.hussein.jetnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.hussein.jetnotes.presentation.main_destinations.util.ThemeStates
import com.hussein.jetnotes.presentation.navigation.AppNavigation
import com.hussein.jetnotes.ui.theme.JetNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as JetNotesApp).appContainer
        val appPreferences = appContainer.appPreferences
        enableEdgeToEdge()
        setContent {
            val themeState by produceState(ThemeStates.SYSTEM_DEFAULT) {
                appPreferences.getDarkModePreference.collect {
                    value = it
                }
            }

            val dynColorEnabled by produceState(true) {
                appPreferences.getDynamicColorPreference.collect {
                    value = it
                }
            }
            JetNotesTheme(
                darkTheme = when (themeState) {
                    ThemeStates.LIGHT -> false
                    ThemeStates.DARK -> true
                    ThemeStates.SYSTEM_DEFAULT -> isSystemInDarkTheme()
                }, dynamicColor = dynColorEnabled
            ) {
                AppNavigation(appContainer = appContainer)
            }
        }
    }
}

