package com.hussein.jetnotes.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hussein.jetnotes.presentation.secret_notes.SecretNotesViewModel
import com.hussein.jetnotes.presentation.secret_notes.notes.SecretNotes
import com.hussein.jetnotes.presentation.secret_notes.passcode.PasscodeScreen
import kotlinx.serialization.Serializable

@Serializable
data object SecretNotesNavGraph


sealed interface SecretNotesDestinations {

    @Serializable
    data object SecretNotes : SecretNotesDestinations

    @Serializable
    data object Passcode : SecretNotesDestinations
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.secretNotesNavGraph(navController: NavController , sharedTransitionScope: SharedTransitionScope) {
    navigation<SecretNotesNavGraph>(startDestination = SecretNotesDestinations.Passcode) {
        composable<SecretNotesDestinations.Passcode> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(SecretNotesNavGraph)
            }
            val viewmodel: SecretNotesViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = SecretNotesViewModel.Factory
            )
            val state = viewmodel.state.collectAsStateWithLifecycle()

            PasscodeScreen(
                state = state.value,
                onAction = viewmodel::onAction,
                onNavigateToSecretNotesScreen = {
                    navController.navigate(SecretNotesDestinations.SecretNotes) {
                        popUpTo(SecretNotesNavGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<SecretNotesDestinations.SecretNotes> {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(SecretNotesNavGraph)
            }
            val viewmodel: SecretNotesViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = SecretNotesViewModel.Factory
            )
            val state = viewmodel.state.collectAsStateWithLifecycle()

            SecretNotes(
                state =state.value ,
                onNavigateToEdit = {},
                onAction = viewmodel::onAction,
                animatedContentScope = this,
                sharedTransitionScope = sharedTransitionScope
            )
        }


    }

}

