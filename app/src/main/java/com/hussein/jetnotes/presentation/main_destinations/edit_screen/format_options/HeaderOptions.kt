package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.format_options

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp

enum class HeaderOptions(val style: SpanStyle) {

    H1(SpanStyle(fontSize = 96.sp)),
    H2(SpanStyle(fontSize = 60.sp)),
    H3(SpanStyle(fontSize = 48.sp)),
    H4(SpanStyle(fontSize = 34.sp)),
    H5(SpanStyle(fontSize = 24.sp)),
    H6(SpanStyle(fontSize = 20.sp));
}