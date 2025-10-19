package com.hussein.jetnotes.presentation.main_app_destinations.main_screen.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SwipeableNoteListItem(
    modifier: Modifier = Modifier,
    onItemDelete: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    isTransitionInProcess: Boolean,
    content: @Composable () -> Unit
) {
    val swipeBoxState = rememberSwipeToDismissBoxState(
        positionalThreshold = { 0.6f },
        initialValue = SwipeToDismissBoxValue.Settled,
    )
    LaunchedEffect(swipeBoxState.currentValue) {
        if (swipeBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onItemDelete()
        }
    }

    SwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        state = swipeBoxState,
        modifier = modifier,
        backgroundContent = {
            if (!isTransitionInProcess) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = shape)
                        .background(
                            color = lerp(
                                MaterialTheme.colorScheme.errorContainer,
                                MaterialTheme.colorScheme.error,
                                swipeBoxState.progress
                            )
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_app_badging_24),
                        contentDescription = "Delete note",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp),
                        tint = Color.White
                    )
                }

            }
        }) { content() }
}