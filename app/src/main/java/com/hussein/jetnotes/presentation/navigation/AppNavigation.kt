package com.hussein.jetnotes.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hussein.jetnotes.data.AppContainer


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navigator = remember(navController) { AppNavigator(navController) }
    SharedTransitionLayout {

        NavHost(navController = navController, startDestination = MainAppDestinations.Main) {

            mainScreenDestination(
                onNavigateToEdit = { id -> navigator.navigateEditScreen(id ?: -1) },
                onNavigateToPasscodeSetup = { navigator.navigateToSecretNotes() },
                onNavigateToSettings = { navigator.navigateToSettings() },
                sharedElementTransitionScope = this@SharedTransitionLayout)

            editScreenDestination(
                onNavigateBack = { navigator.navigateToMainScreen() },
                sharedElementTransitionScope = this@SharedTransitionLayout
            )

            settingsDestination(onNavigateBack = { navigator.navigateToMainScreen() })

            secretNotesNavGraph(navController = navController , sharedTransitionScope = this@SharedTransitionLayout)
        }
    }
}