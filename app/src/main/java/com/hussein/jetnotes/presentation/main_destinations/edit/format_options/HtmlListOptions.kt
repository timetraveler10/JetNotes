package com.hussein.jetnotes.presentation.main_destinations.edit.format_options

import androidx.annotation.DrawableRes
import com.hussein.jetnotes.R

enum class HtmlListOptions(@DrawableRes val icon: Int, val displayName: String) {
    Ordered(R.drawable.format_list_numbered, "Ordered"),
    Unordered(R.drawable.format_list_bulleted, "Unordered")
}