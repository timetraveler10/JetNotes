package com.hussein.jetnotes.presentation.secret_notes.passcode

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasscodeScreenTopAppBar(title: String) {
    MediumTopAppBar(
        title = {
            Text(title)
        }, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}