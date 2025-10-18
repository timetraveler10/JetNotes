package com.hussein.jetnotes.presentation.shared_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    onAddNewCategoryClick: () -> Unit,
    isLoading: Boolean
) {

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        FilterChip(
            modifier = Modifier.animateContentSize(),
            selected = currentSelectedCategoryId == null && !isLoading,
            onClick = { onCategorySelected(null) },
            label = { Text(text = "All") },
            trailingIcon = { CategoryChipIcon(currentSelectedCategoryId == null) })

        categories.forEach { category ->
            FilterChip(
                modifier = Modifier.animateContentSize(),
                selected = currentSelectedCategoryId == category.id, onClick = {
                if (currentSelectedCategoryId != category.id) {
                    onCategorySelected(category.id)
                }

            }, label = { Text(text = category.name) },
                trailingIcon = { CategoryChipIcon(currentSelectedCategoryId == category.id) })
        }

        AssistChip(
            onClick = onAddNewCategoryClick, label = {
                Icon(
                    painter = painterResource(R.drawable.outline_add_24), contentDescription = null
                )
            })
    }
}

@Composable
private fun CategoryChipIcon(isSelected: Boolean) {
    AnimatedVisibility(
        isSelected,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Icon(
            painter = painterResource(R.drawable.checkmark),
            contentDescription = null
        )
    }
}