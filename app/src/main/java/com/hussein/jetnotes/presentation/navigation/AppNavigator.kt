package com.hussein.jetnotes.presentation.navigation

import androidx.navigation.NavController

class AppNavigator(val navController: NavController) {
    val navigateToMainScreen: () -> Unit = {
        navController.navigate(MainAppDestinations.Main) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true

        }
    }

    val navigateEditScreen: (Int) -> Unit = { id->
        navController.navigate(MainAppDestinations.Edit(id))
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(MainAppDestinations.Settings)
    }
    val navigateToSecretNotes: () -> Unit = {
        navController.navigate(SecretNotesDestinations.Passcode)
    }
}