package com.hussein.jetnotes.presentation.main_screen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.hussein.jetnotes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopAppBar(
    modifier: Modifier = Modifier,
    onNavigateToPasscodeSetup: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(modifier = modifier, title = {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Medium, color =
                            MaterialTheme.colorScheme.primary
                    ),
                ) {
                    append("Jet")
                }
                append("Notes")
            },
        )
    }, scrollBehavior = scrollBehavior, actions = {
        FilledTonalIconButton(onClick = onNavigateToPasscodeSetup) {
            Icon(
                painter = painterResource(R.drawable.outline_app_badging_24),
                contentDescription = ""
            )
        }
    })
}