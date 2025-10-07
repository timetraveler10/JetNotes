package com.hussein.jetnotes.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.main_screen.components.AddFab
import com.hussein.jetnotes.presentation.main_screen.components.MainScreenSheetContent
import com.hussein.jetnotes.presentation.main_screen.components.MainScreenTopAppBar
import com.hussein.jetnotes.presentation.main_screen.components.NoteListItem
import com.hussein.jetnotes.presentation.shared_components.CategoryBar
import com.hussein.jetnotes.presentation.util.EmptyContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onAction: (MainScreenActions) -> Unit,
    onNavigate: (Int?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(state.selectedCategoryId) {
        if (!state.isLoading) {
            onAction(MainScreenActions.LoadData(state.selectedCategoryId))
        }
        onAction(MainScreenActions.LoadCategories)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainScreenTopAppBar(scrollBehavior = scrollBehavior) },
        floatingActionButton = { AddFab(onClick = { onNavigate(null) }) }
    ) { innerPadding ->


        if (!state.isLoading) {
            if (state.notes.isEmpty()) {
                EmptyContent(message = "No Notes Found")
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
            ) {
                MainScreenSheetContent(onAddNewCategory = {
                    onAction(MainScreenActions.AddNewCategory(it))
                    scope.launch { sheetState.hide() }
                })
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                CategoryBar(
                    categories = state.categories,
                    currentSelectedCategoryId = state.selectedCategoryId,
                    onCategorySelected = {
                        onAction(MainScreenActions.SelectCategory(it))
                    },
                    onAddNewCategory = { showBottomSheet = true })
            }
            items(state.notes) {
                NoteListItem(

                    note = it,
                    onCheckedChange = {},
                    onItemClicked = { id -> onNavigate(id) })
            }
        }

    }

}




