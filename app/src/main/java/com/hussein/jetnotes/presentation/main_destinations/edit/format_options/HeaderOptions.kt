package com.hussein.jetnotes.presentation.main_destinations.edit.format_options

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class HeaderOptions(val style: SpanStyle) {

    H1(SpanStyle(fontSize = 96.sp, fontWeight = FontWeight.ExtraBold)),
    H2(SpanStyle(fontSize = 60.sp, fontWeight = FontWeight.Bold)),
    H3(SpanStyle(fontSize = 48.sp, fontWeight = FontWeight.SemiBold)),
}