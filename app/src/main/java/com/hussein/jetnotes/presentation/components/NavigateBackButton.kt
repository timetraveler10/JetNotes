package com.hussein.jetnotes.presentation.shared_components

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hussein.jetnotes.R

@Composable
fun NavigateBackButton(onNavigateBack:()-> Unit) {
    FilledTonalIconButton(onClick = onNavigateBack) {
        Icon(
            painter = painterResource(R.drawable.outline_arrow_back_ios_new_24),
            contentDescription = null
        )
    }
}