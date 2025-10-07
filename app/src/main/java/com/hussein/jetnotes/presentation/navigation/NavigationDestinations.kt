package com.hussein.jetnotes.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hussein.jetnotes.presentation.edit_screen.EditScreen
import com.hussein.jetnotes.presentation.edit_screen.EditViewModel
import com.hussein.jetnotes.presentation.main_screen.MainScreen
import com.hussein.jetnotes.presentation.main_screen.MainViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Main

@Serializable
data class Edit(val id: Int)

@Serializable
data object SecretNotes

fun NavGraphBuilder.mainScreenDestination(onNavigateToEdit: (Int?) -> Unit) {

    composable<Main> {
        val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
        val state = mainViewModel.state.collectAsStateWithLifecycle()

        MainScreen(
            state = state.value,
            onAction = mainViewModel::onAction,
            onNavigate = onNavigateToEdit
        )
    }
}

fun NavGraphBuilder.editScreenDestination(onNavigateBack: () -> Unit) {

    composable<Edit> { backStackEntry ->
        val editViewModel: EditViewModel = viewModel(factory = EditViewModel.Factory)
        val state = editViewModel.state.collectAsStateWithLifecycle()
        val arg: Edit = backStackEntry.toRoute()

        EditScreen(
            state = state.value,
            onAction = editViewModel::onAction,
            id = arg.id,
            navigateBack = onNavigateBack
        )
    }
}