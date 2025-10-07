package com.hussein.jetnotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hussein.jetnotes.data.AppContainer


@Composable
fun AppNavigation(appContainer: AppContainer) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Main) {

        mainScreenDestination(onNavigateToEdit = { id ->
            navController.navigate(Edit(id ?: -1))
        })

        editScreenDestination(onNavigateBack = {
            navController.navigateUp()
        })

    }
}