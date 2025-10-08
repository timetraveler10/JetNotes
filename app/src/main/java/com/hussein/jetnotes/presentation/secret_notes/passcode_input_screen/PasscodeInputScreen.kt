package com.hussein.jetnotes.presentation.secret_notes_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.secret_notes_screen.components.PasscodeTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeInputScreen(
    modifier: Modifier = Modifier,
    state: SecretNotesScreenState,
    onComplete: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            LargeTopAppBar(title = { Text(text = "Please enter your passcode") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            PasscodeTextField(
                onPasscodeChange = { },
                onComplete = { },
                passcode = "",
            )
        }
    }
}



