package com.hussein.jetnotes.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hussein.jetnotes.presentation.main_destinations.edit.EditScreen
import com.hussein.jetnotes.presentation.main_destinations.edit.EditViewModel
import com.hussein.jetnotes.presentation.main_destinations.notes.NotesScreen
import com.hussein.jetnotes.presentation.main_destinations.notes.NotesViewModel
import com.hussein.jetnotes.presentation.main_destinations.settings.SettingsScreen
import com.hussein.jetnotes.presentation.main_destinations.settings.SettingsScreenViewModel
import kotlinx.serialization.Serializable


@Serializable
data object MainNavGraph

@Serializable
sealed interface MainAppDestinations {
    @Serializable
    data object Main : MainAppDestinations

    @Serializable
    data class Edit(val id: Int) : MainAppDestinations
@Serializable
    data object Settings : MainAppDestinations
    data object Reminders : MainAppDestinations
}


@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.mainScreenDestination(
    onNavigateToEdit: (Int?) -> Unit,
    onNavigateToPasscodeSetup: () -> Unit,
    onNavigateToSettings:()->Unit,
    sharedElementTransitionScope: SharedTransitionScope
) {

    composable<MainAppDestinations.Main> {
        val notesViewModel: NotesViewModel = viewModel(factory = NotesViewModel.Factory)
        val state = notesViewModel.state.collectAsStateWithLifecycle()

        NotesScreen(
            state = state.value,
            onAction = notesViewModel::onAction,
            onNavigate = onNavigateToEdit,
            onNavigateToPasscodeSetup = onNavigateToPasscodeSetup,
            animatedContentScope = this,
            sharedElementTransitionScope = sharedElementTransitionScope,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.editScreenDestination(
    onNavigateBack: () -> Unit,
    sharedElementTransitionScope: SharedTransitionScope
) {

    composable<MainAppDestinations.Edit> { backStackEntry ->
        val editViewModel: EditViewModel = viewModel(factory = EditViewModel.Factory)
        val state = editViewModel.state.collectAsStateWithLifecycle()
        val arg: MainAppDestinations.Edit = backStackEntry.toRoute()

        EditScreen(
            state = state.value,
            onAction = editViewModel::onAction,
            id = arg.id,
            navigateBack = onNavigateBack,
            sharedElementTransitionScope = sharedElementTransitionScope,
            animatedContentScope = this
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.settingsDestination(onNavigateBack: () -> Unit) {

    composable<MainAppDestinations.Settings> { backStackEntry ->
        val settingsScreenViewModel: SettingsScreenViewModel =
            viewModel(factory = SettingsScreenViewModel.Factory)
        val state = settingsScreenViewModel.state.collectAsStateWithLifecycle()

        SettingsScreen(
            state = state.value,
            onAction = settingsScreenViewModel::onAction, onNavigateBack = onNavigateBack
        )
    }
}