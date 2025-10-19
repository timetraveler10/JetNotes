package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hussein.jetnotes.R
import com.hussein.jetnotes.presentation.components.NavigateBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onClickOnShowMore: () -> Unit

) {
    TopAppBar(
        title = {},
        navigationIcon = {
            NavigateBackButton(onNavigateBack = onNavigateBack)
        },
        actions = {
        FilledTonalIconButton(onClick = onClickOnShowMore) {
            Icon(
                painter = painterResource(R.drawable.more_vert),
                contentDescription = null
            )
        }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow))
}