package com.hussein.jetnotes.presentation.main_screen.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hussein.jetnotes.R

@Composable
fun AddFab(modifier: Modifier = Modifier, onClick: () -> Unit) {
    LargeFloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = null)
    }
}