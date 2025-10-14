package com.hussein.jetnotes.presentation.edit_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.AppInputTextField
import com.hussein.jetnotes.presentation.edit_screen.components.EditScreenTopAppBar
import com.hussein.jetnotes.presentation.main_screen.components.MainScreenSheetContent
import com.hussein.jetnotes.presentation.shared_components.CategoryBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    state: EditScreenState,
    onAction: (EditScreenActions) -> Unit,
    id: Int = -1,
    navigateBack: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun onNavigateBack() {
        onAction(EditScreenActions.SaveNote)
        navigateBack()
    }


    BackHandler(enabled = true) {
        onNavigateBack()
    }

    LaunchedEffect(id) {
        if (id != -1) {
            onAction(EditScreenActions.LoadNote(id))
        }
        onAction(EditScreenActions.LoadCategories)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EditScreenTopAppBar(onNavigateBack = ::onNavigateBack)
        }) { innerPadding ->


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
            ) {
                MainScreenSheetContent(
                    onAddNewCategory = {
                        onAction(EditScreenActions.AddNewCategory(it))
                        scope.launch { sheetState.hide() }
                    })
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text("Selected Category", style = MaterialTheme.typography.titleSmall)
            CategoryBar(
                modifier = Modifier.fillMaxWidth(),
                categories = state.categories,
                currentSelectedCategoryId = state.categoryId,
                onAddNewCategory = {
                    showBottomSheet = true
                },
                onCategorySelected = {
                    onAction(EditScreenActions.SelectCategory(it))
                })
            AppInputTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state.title,
                onValueChange = { onAction(EditScreenActions.UpdateTitle(it)) },
                textStyle = MaterialTheme.typography.headlineMedium,
                placeHolderText = "Title",
                maxLines = 2
            )
            AppInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = state.content,
                onValueChange = { onAction(EditScreenActions.UpdateContent(it)) },
                textStyle = MaterialTheme.typography.bodyLarge,
                placeHolderText = "Content",
                maxLines = Int.MAX_VALUE,

                )
        }


    }
}

