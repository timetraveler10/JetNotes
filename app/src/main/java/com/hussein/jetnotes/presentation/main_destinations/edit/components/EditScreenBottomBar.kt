package com.hussein.jetnotes.presentation.main_destinations.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun EditScreenBottomBar(
    modifier: Modifier = Modifier,
    onFormatClick: () -> Unit,
    onAiAssistanceClick: () -> Unit
) {
    Surface(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = onFormatClick) {
                Icon(
                    painter = painterResource(com.hussein.jetnotes.R.drawable.text_format,),
                    contentDescription = "Text format options"
                )
            }
            IconButton(onClick = onAiAssistanceClick) {
                Icon(
                    painter = painterResource(com.hussein.jetnotes.R.drawable.ai_placeholder),
                    contentDescription = "Text format options"
                )
            }
        }
    }
}