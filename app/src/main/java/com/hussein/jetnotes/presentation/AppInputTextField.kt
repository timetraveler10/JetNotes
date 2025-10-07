package com.hussein.jetnotes.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AppInputTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    placeHolderText: String,
    maxLines: Int,
) {
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        textStyle = textStyle,
        maxLines = maxLines,

        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(text.isEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    BasicText(
                        text = placeHolderText,
                        style = textStyle
                    )

                }
                innerTextField()
            }
        },
    )

}