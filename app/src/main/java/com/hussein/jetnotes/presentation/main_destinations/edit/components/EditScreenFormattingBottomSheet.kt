package com.hussein.jetnotes.presentation.main_destinations.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlListOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlParagraphOptions
import com.hussein.jetnotes.presentation.main_destinations.edit.format_options.HtmlTextSpanOptions

@Composable
fun EditScreenFormattingBottomSheet(
    isBoldSelected: Boolean,
    isItalicSelected: Boolean,
    isUnderlinedSelected: Boolean,
    isLineThroughSelected: Boolean,
    isCenterAlignedSelected: Boolean = false,
    isRightAlignedSelected: Boolean = false,
    isLeftAlignedSelected: Boolean = false,
    isOrderedListSelected: Boolean = false,
    isUnorderedListSelected: Boolean = false,
    onSpanOptionSelected: (HtmlTextSpanOptions) -> Unit,
    onMarkDownParagraphOptionSelected: (HtmlParagraphOptions) -> Unit,
    onToggleListOption: (HtmlListOptions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TODO heading options

        Text(text = "Span Options" , style = MaterialTheme.typography.labelMedium)
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            // bold button
            HtmlTextSpanOptions.entries.forEachIndexed { index, option ->
                val isSelected = when (option) {
                    HtmlTextSpanOptions.BOLD -> isBoldSelected
                    HtmlTextSpanOptions.ITALIC -> isItalicSelected
                    HtmlTextSpanOptions.UNDERLINE -> isUnderlinedSelected
                    HtmlTextSpanOptions.LINE_THROUGH -> isLineThroughSelected
                }

                SegmentedButton(
                    contentPadding = PaddingValues(0.dp),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = HtmlTextSpanOptions.entries.size
                    ),
                    checked = isSelected,
                    onCheckedChange = { onSpanOptionSelected(option) },
                    label = {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = option.displayName
                        )
                    }
                )

            }


        }
        // paragraph alignment options
        Text(text = "Paragraph options" , style = MaterialTheme.typography.labelMedium)
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            HtmlParagraphOptions.entries.forEachIndexed { index, option ->
                val isSelected = when (option) {
                    HtmlParagraphOptions.AlignLeft -> isLeftAlignedSelected
                    HtmlParagraphOptions.AlignRight -> isRightAlignedSelected
                    HtmlParagraphOptions.AlignCenter -> isCenterAlignedSelected
                }
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = HtmlParagraphOptions.entries.size
                    ),
                    checked = isSelected,
                    onCheckedChange = {
                        onMarkDownParagraphOptionSelected(option)
                    },
                    label = {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = ""
                        )
                    }
                )
            }
        }
        // list options
        Text(text = "List options" , style = MaterialTheme.typography.labelMedium)
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            HtmlListOptions.entries.forEachIndexed { index, option ->
                val isSelected = when (option) {
                    HtmlListOptions.Ordered -> isOrderedListSelected
                    HtmlListOptions.Unordered -> isUnorderedListSelected
                }
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = HtmlListOptions.entries.size
                    ),
                    checked = isSelected,
                    onCheckedChange = {
                        onToggleListOption(option)
                    },
                    label = {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = ""
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditScreenFormattingBottomSheetPrev() {
    EditScreenFormattingBottomSheet(
        isBoldSelected = true,
        isItalicSelected = false,
        isUnderlinedSelected = false,
        isLineThroughSelected = true,
        onSpanOptionSelected = { },
        isCenterAlignedSelected = false,
        isRightAlignedSelected = true,
        isLeftAlignedSelected = false,
        onMarkDownParagraphOptionSelected = { },
        isOrderedListSelected = true,
        isUnorderedListSelected = true,
        onToggleListOption = {},
    )
}