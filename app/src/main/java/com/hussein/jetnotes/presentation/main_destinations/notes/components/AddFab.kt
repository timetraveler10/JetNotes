package com.hussein.jetnotes.presentation.main_destinations.notes.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hussein.jetnotes.R

@Composable
fun AddFab(modifier: Modifier = Modifier, onClick: () -> Unit, shouldShow: Boolean) {
    AnimatedVisibility(
        visible = shouldShow,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut() , label = "main_screen_fab"
    ) {

        LargeFloatingActionButton(modifier = modifier, onClick = onClick) {
            Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = null)
        }

    }
}