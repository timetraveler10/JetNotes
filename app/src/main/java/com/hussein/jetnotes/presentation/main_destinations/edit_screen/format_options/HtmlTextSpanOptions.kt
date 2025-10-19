package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.format_options

import androidx.annotation.DrawableRes
import com.hussein.jetnotes.R

enum class HtmlTextSpanOptions(@DrawableRes val icon: Int, val displayName: String) {
    BOLD(R.drawable.format_bold, "Bold"),
    ITALIC(R.drawable.format_italic, "Italic"),
    UNDERLINE(R.drawable.format_underlined, "Underline"),
    LINE_THROUGH(R.drawable.format_strikethrough, "Line Through"),
}