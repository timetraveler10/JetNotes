package com.hussein.jetnotes.presentation.edit_screen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hussein.jetnotes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenTopAppBar(
    modifier: Modifier = Modifier, onNavigateBack: () -> Unit,

) {
    TopAppBar(title = {}, navigationIcon = {
        FilledTonalIconButton(onClick = onNavigateBack) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_ios_new_24),
                contentDescription = null
            )
        }
    }, actions = {
//        FilledTonalIconButton(onClick = onInsert) {
//            Icon(
//                painter = painterResource(R.drawable.outline_add_24),
//                contentDescription = null
//            )
//        }
    })
}