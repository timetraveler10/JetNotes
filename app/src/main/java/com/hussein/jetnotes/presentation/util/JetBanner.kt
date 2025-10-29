package com.hussein.jetnotes.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

@Composable
fun JetBanner(
    visible: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    text: String,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    borderStroke: BorderStroke? = BorderStroke(
        1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
) {

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .then(modifier),
            color = color,
            shape = shape,
            border = borderStroke
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )
            }
        }

    }
}

@Preview
@Composable
private fun JetBannerPrev() {
    JetBanner(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.errorContainer,
        text = LoremIpsum(100).values.joinToString(" "),
        borderStroke = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        visible = true
    )
}

