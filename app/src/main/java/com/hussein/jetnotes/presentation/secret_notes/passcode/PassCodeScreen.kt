package com.hussein.jetnotes.presentation.secret_notes.pre_secret_notes_entry

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.secret_notes.PassCodeInputState
import com.hussein.jetnotes.presentation.secret_notes.SecretNoteActions
import com.hussein.jetnotes.presentation.secret_notes.SecretNotesScreenState
import com.hussein.jetnotes.presentation.secret_notes.secret_notes_screen.PassCodeScreenTopAppBar
import com.hussein.jetnotes.presentation.secret_notes.secret_notes_screen.components.PasscodeTextField

@Composable
fun PassCodeScreen(
    state: SecretNotesScreenState,
    onAction: (SecretNoteActions) -> Unit,
    onNavigateToSecretNotesScreen: () -> Unit
) {

    var entryState by remember { mutableStateOf(PassCodeInputState.Loading) }

    LaunchedEffect(Unit) {
        onAction(SecretNoteActions.CheckIfPasscodeExists)
    }

    LaunchedEffect(state) {
        if (state.isAuthSuccessful){
            onNavigateToSecretNotesScreen.invoke()
        }
    }
    LaunchedEffect(state.isPasscodeSet) {
        if (state.isPasscodeSet) {
            entryState = PassCodeInputState.Input
        }
    }

    // This effect runs once when the composable enters the screen
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            val isPasscodeSet = state.isPasscodeSet
            entryState = if (isPasscodeSet) PassCodeInputState.Input else PassCodeInputState.Setup

        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        when (entryState) {
            PassCodeInputState.Setup -> {
                PassCodeScreenTopAppBar(
                    title = when {
                        state.isPasscodeSet -> "Passcode Saved"
                        state.isConfirmationStep -> "Confirm Passcode"
                        else -> "Setup Passcode"
                    }
                )
            }

            PassCodeInputState.Input -> PassCodeScreenTopAppBar(title = "Please enter your passcode")
            PassCodeInputState.Loading -> PassCodeScreenTopAppBar(title = "Loading...")
        }
    }) { innerPadding ->
        AnimatedContent(
            modifier = Modifier.padding(innerPadding),
            targetState = entryState
        ) { inputState ->
            if (inputState == PassCodeInputState.Loading) LoadingContent()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
            ) {

                PasscodeTextField(
                    onPasscodeChange = {
                        onAction(SecretNoteActions.OnPasscodeInputChange(it))
                    },
                    onComplete = {
                        if (entryState == PassCodeInputState.Input) onAction(
                            SecretNoteActions.HandlePasscodeEntrySubmit(it)
                        )
                        else onAction(SecretNoteActions.HandlePasscodeSetupSubmit)
                    },
                    passcode = state.currentInput,
                    showError = state.passcodeErrorMessage != null,
                    showSuccess = state.isAuthSuccessful
                )
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

}