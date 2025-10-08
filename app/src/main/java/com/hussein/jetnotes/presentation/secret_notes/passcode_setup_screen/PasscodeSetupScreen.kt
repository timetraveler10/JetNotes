package com.hussein.jetnotes.presentation.passcode_setup_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.secret_notes_screen.components.PasscodeTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeSetupScreen(
    modifier: Modifier = Modifier,
    onAction: (PasscodeSetupActions) -> Unit,
    state: PasscodeSetupState,
    onNavigateToPasscodeSecretNotes: () -> Unit
) {
    LaunchedEffect(state) {
        if (state.isPasscodeSaved) {
            onNavigateToPasscodeSecretNotes.invoke()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            LargeTopAppBar(title = {
                when {
                    state.isPasscodeSaved -> Text(text = "Passcode Saved")
                    state.isConfirmationStep -> Text(text = "Confirm Passcode")
                    state.errorMessage != null -> Text(text = state.errorMessage)
                    else -> Text(text = "Enter Passcode")
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            PasscodeTextField(
                onPasscodeChange = { onAction(PasscodeSetupActions.OnInputChange(it)) },
                onComplete = { onAction(PasscodeSetupActions.HandleSubmit) },
                passcode = state.currentInput,
            )
        }
    }
}