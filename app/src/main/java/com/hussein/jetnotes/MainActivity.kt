package com.hussein.jetnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hussein.jetnotes.presentation.navigation.AppNavigation
import com.hussein.jetnotes.ui.theme.JetNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as JetNotesApp).appContainer
        enableEdgeToEdge()
        setContent {
            JetNotesTheme {
                AppNavigation(appContainer = appContainer)
            }
        }
    }
}

