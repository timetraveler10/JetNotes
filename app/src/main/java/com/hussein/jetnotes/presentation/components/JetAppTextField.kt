package com.hussein.jetnotes.presentation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun JetAppTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    maxLines: Int,
    placeHolderText: String,
    contentPaddingValues: PaddingValues = PaddingValues(4.dp)
) {
    BasicTextField(
        maxLines = maxLines,
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary), decorationBox = {
            TextFieldDefaults.DecorationBox(
                value = text,
                innerTextField = it,
                enabled = true,
                singleLine = maxLines == 1,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                isError = false,
                placeholder = {
                    Text(
                        placeHolderText,
                        style = textStyle
                    )
                },
                contentPadding = contentPaddingValues,
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    )
//    TextField(
//        modifier = modifier,
//        value = text,
//        onValueChange = onValueChange,
//        textStyle = textStyle,
//        maxLines = maxLines,
//    )

}