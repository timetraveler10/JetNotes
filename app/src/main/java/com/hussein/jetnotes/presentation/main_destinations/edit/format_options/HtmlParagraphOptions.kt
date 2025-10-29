package com.hussein.jetnotes.presentation.main_destinations.edit.format_options

import androidx.annotation.DrawableRes
import com.hussein.jetnotes.R

enum class HtmlParagraphOptions(@DrawableRes val icon: Int, val displayName: String) {
    AlignLeft(R.drawable.format_align_left, "Align Left"),
    AlignCenter(R.drawable.format_align_center, "Align Center"),
    AlignRight(R.drawable.format_align_right, "Align Right")
}