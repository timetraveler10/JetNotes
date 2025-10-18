package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R
import com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.MarkupTextOptions

@Composable
fun EditScreenFormattingBottomSheet(
    modifier: Modifier = Modifier,
    isBoldSelected: Boolean,
    isItalicSelected: Boolean,
    isUnderlinedSelected: Boolean,
    isLineThroughSelected: Boolean,
    onSelectLineThrough: () -> Unit,
    onSelectBold: () -> Unit,
    onSelectItalic: () -> Unit,
    onSelectUnderlined: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TODO heading options
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            // bold button
            MarkupTextOptions.entries.forEachIndexed { index, option ->
                val isSelected = when (option) {
                    MarkupTextOptions.BOLD -> isBoldSelected
                    MarkupTextOptions.ITALIC -> isItalicSelected
                    MarkupTextOptions.UNDERLINE -> isUnderlinedSelected
                    MarkupTextOptions.LINE_THROUGH -> isLineThroughSelected
                }
                val onSelectOption = when (option) {
                    MarkupTextOptions.BOLD -> onSelectBold
                    MarkupTextOptions.ITALIC -> onSelectItalic
                    MarkupTextOptions.UNDERLINE -> onSelectUnderlined
                    MarkupTextOptions.LINE_THROUGH -> onSelectLineThrough
                }
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = MarkupTextOptions.entries.size
                    ),
                    checked = isSelected,
                    onCheckedChange = { onSelectOption() },
                    label = {
                        Text(
                            option.displayName,
                            maxLines = 1,
                            overflow = TextOverflow.MiddleEllipsis
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(option.icon),
                            contentDescription = option.displayName
                        )
                    }
                )

            }


        }

        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            // align left
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 3
                ),
                checked = true,
                onCheckedChange = { },
                label = { Text("Bold") }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.format_align_left),
                        contentDescription = ""
                    )
                }
            )
            // align center
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 3
                ),
                checked = true, onCheckedChange = { },
                label = { Text("Center") }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.format_align_center),
                        contentDescription = ""
                    )
                }
            )
            // align right
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 2,
                    count = 3
                ),
                checked = true, onCheckedChange = { },
                label = { Text("Right") }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.format_align_right),
                        contentDescription = ""
                    )
                }
            )

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
        onSelectLineThrough = { },
        onSelectBold = { },
        onSelectItalic = {},
        onSelectUnderlined = {}
    )
}