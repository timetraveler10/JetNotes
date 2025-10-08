package com.hussein.jetnotes.presentation.secret_notes_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PasscodeTextField(
    passcode:String ,
    onPasscodeChange:(String) ->Unit ,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit,
    inputLength: Int = 4
) {
    LaunchedEffect(passcode) {
        if (passcode.length == inputLength) {
            onComplete.invoke(passcode)
        }
    }

    BasicTextField(
        value = passcode,
        onValueChange = { if (it.length <= inputLength) onPasscodeChange(it) },
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(inputLength) { index ->
                    val char = when {
                        index >= passcode.length -> "-"
                        else -> "*"
                    }
                    val isFocused = passcode.length == index
                    val transition = updateTransition(isFocused)

                    val borderWith by transition.animateDp(label = "borderWidth") {
                        if (it) 2.dp else 1.dp
                    }
                    val borderColor by transition.animateColor(label = "borderColor") {
                        if (it) MaterialTheme.colorScheme.outline else
                            MaterialTheme.colorScheme.outlineVariant
                    }
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(56.dp)
                            .border(
                                width = borderWith,
                                color = borderColor,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(targetState = char ) {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                }
            }
        })

}


@Preview
@Composable
private fun PasscodeTextFieldPrev() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PasscodeTextField(onComplete = {} , passcode = "1243" , onPasscodeChange = {})
    }
}