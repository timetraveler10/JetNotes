package com.hussein.jetnotes.presentation.main_app_destinations.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorColors
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetRichTextField(
    modifier: Modifier = Modifier,
    color: RichTextEditorColors = RichTextEditorDefaults.richTextEditorColors(),
    richTextState: RichTextState = rememberRichTextState()
) {

    val isBoldSelected = richTextState.currentSpanStyle.fontWeight == FontWeight.Bold
    val isItalicSelected = richTextState.currentSpanStyle.fontStyle == FontStyle.Italic

    val isUnderlinedSelected =
        richTextState.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline)
    val isLineThroughSelected =
        richTextState.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough)

    Box(modifier = modifier) {
        RichTextEditor(
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxSize(),
            state = richTextState,
            colors = color
        )
        FormatRow(
            modifier = Modifier
                .wrapContentWidth()
                .animateContentSize()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            isBoldSelected = isBoldSelected,
            isItalicSelected = isItalicSelected,
            isUnderlinedSelected = isUnderlinedSelected == true,
            isLineThroughSelected = isLineThroughSelected == true,
            onSelectBold = {
                richTextState.toggleSpanStyle(spanStyle = SpanStyle(fontWeight = FontWeight.Bold))
            },
            onSelectItalic = {
                richTextState.toggleSpanStyle(spanStyle = SpanStyle(fontStyle = FontStyle.Italic))
            },
            onSelectUnderlined = {
                richTextState.toggleSpanStyle(spanStyle = SpanStyle(textDecoration = TextDecoration.Underline))
            },
            onSelectLineThrough = {
                richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            }
        )
    }
}

@Composable
fun FormatRow(
    modifier: Modifier = Modifier,
    isBoldSelected: Boolean,
    isItalicSelected: Boolean,
    isUnderlinedSelected: Boolean,
    isLineThroughSelected: Boolean,
    onSelectLineThrough: () -> Unit,
    onSelectBold: () -> Unit,
    onSelectItalic: () -> Unit,
    onSelectUnderlined: () -> Unit
) {
    var showFormatOptions by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            AnimatedVisibility(showFormatOptions) {
                Surface(
                    modifier = Modifier.wrapContentWidth(),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // sub text format options
                        FormatOption(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.format_bold),
                                    contentDescription = ""
                                )
                            }, onToggle = onSelectBold,
                            isOptionSelected = isBoldSelected
                        )
                        FormatOption(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.format_italic),
                                    contentDescription = ""
                                )
                            }, onToggle = onSelectItalic,
                            isOptionSelected = isItalicSelected
                        )
                        FormatOption(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.format_strikethrough),
                                    contentDescription = ""
                                )
                            }, onToggle = onSelectLineThrough,
                            isOptionSelected = isLineThroughSelected
                        )
                        FormatOption(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.format_underlined),
                                    contentDescription = ""
                                )
                            }, onToggle = onSelectUnderlined,
                            isOptionSelected = isUnderlinedSelected
                        )
                    }
                }
            }
            FilledTonalIconToggleButton(
                onCheckedChange = { showFormatOptions = it },
                checked = showFormatOptions
            ) {
                Icon(painter = painterResource(R.drawable.text_format), contentDescription = "")
            }
        }

    }
}

@Composable
fun FormatOption(icon: @Composable () -> Unit, onToggle: () -> Unit, isOptionSelected: Boolean) {
    FilledTonalIconToggleButton(onCheckedChange = { onToggle() }, checked = isOptionSelected) {
        icon()
    }
}

@Preview()
@Composable
private fun JetRichTextFieldPrev() {
    Column(Modifier.statusBarsPadding()) {
        JetRichTextField()

    }
}