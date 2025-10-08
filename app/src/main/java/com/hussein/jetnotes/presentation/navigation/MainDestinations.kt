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
data object MainNavGraph

@Serializable
sealed interface MainNoteDestinations{
    @Serializable
    data object Main: MainNoteDestinations

    @Serializable
    data class Edit(val id: Int): MainNoteDestinations
}




fun NavGraphBuilder.mainScreenDestination(
    onNavigateToEdit: (Int?) -> Unit,
    onNavigateToPasscodeSetup: () -> Unit
) {

    composable<MainNoteDestinations.Main> {
        val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
        val state = mainViewModel.state.collectAsStateWithLifecycle()

        MainScreen(
            state = state.value,
            onAction = mainViewModel::onAction,
            onNavigate = onNavigateToEdit ,
            onNavigateToPasscodeSetup =onNavigateToPasscodeSetup
        )
    }
}

fun NavGraphBuilder.editScreenDestination(onNavigateBack: () -> Unit) {

    composable<MainNoteDestinations.Edit> { backStackEntry ->
        val editViewModel: EditViewModel = viewModel(factory = EditViewModel.Factory)
        val state = editViewModel.state.collectAsStateWithLifecycle()
        val arg: MainNoteDestinations.Edit = backStackEntry.toRoute()

        EditScreen(
            state = state.value,
            onAction = editViewModel::onAction,
            id = arg.id,
            navigateBack = onNavigateBack
        )
    }
}