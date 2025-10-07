package com.hussein.jetnotes.presentation.shared_components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.R
import com.hussein.jetnotes.data.models.Category

@Composable
fun CategoryBar(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    currentSelectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit,
    onAddNewCategory: () -> Unit
) {

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        FilterChip(
            selected = currentSelectedCategoryId == null,
            onClick = { onCategorySelected(null) },
            label = { Text(text = "All") })

        categories.forEach { category ->
            FilterChip(
                selected = currentSelectedCategoryId == category.id,
                onClick = {
                    if(currentSelectedCategoryId != category.id){
                        onCategorySelected(category.id)
                    }

                },
                label = { Text(text = category.name) }
            )
        }

        AssistChip(
            onClick = onAddNewCategory,
            label = { Icon(painter = painterResource(R.drawable.outline_add_24) , contentDescription = null) })
    }
}