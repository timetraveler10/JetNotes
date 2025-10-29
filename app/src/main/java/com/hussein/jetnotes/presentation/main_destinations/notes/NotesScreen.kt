package com.hussein.jetnotes.presentation.main_destinations.notes

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.hussein.jetnotes.data.models.Note
import com.hussein.jetnotes.presentation.components.CategoryBar
import com.hussein.jetnotes.presentation.main_destinations.notes.components.AddFab
import com.hussein.jetnotes.presentation.main_destinations.notes.components.MainScreenTopAppBar
import com.hussein.jetnotes.presentation.main_destinations.notes.components.NoteListItem
import com.hussein.jetnotes.presentation.main_destinations.shared.NewCategorySheetContent
import com.hussein.jetnotes.presentation.main_destinations.util.ObserveAsEvents
import com.hussein.jetnotes.presentation.main_destinations.util.SnackbarController
import com.hussein.jetnotes.presentation.util.EmptyContent
import com.hussein.jetnotes.presentation.util.NoteListState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun NotesScreen(
    state: NotesScreenState,
    onAction: (NotesScreenActions) -> Unit,
    onNavigate: (Int?) -> Unit,
    onNavigateToPasscodeSetup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedElementTransitionScope: SharedTransitionScope
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(state.selectedCategoryId) {
        if (!state.isLoading) {
            onAction(NotesScreenActions.LoadData(state.selectedCategoryId))
        }
        onAction(NotesScreenActions.LoadCategories)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Long
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    BackHandler(enabled = state.isSelectionModeEnabled) {
        onAction(NotesScreenActions.OnToggleSelectionMode)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainScreenTopAppBar(
                scrollBehavior = scrollBehavior,
                onNavigateToPasscodeSetup = {
                    onNavigateToPasscodeSetup()
                }, onNavigateToSettings = onNavigateToSettings,
                onDiscardSelection = {
                    onAction(NotesScreenActions.OnToggleSelectionMode)
                },
                isSelectionModeEnabled = state.isSelectionModeEnabled,
                selectedNotesCount = state.selectedNotes.size , onDeleteSelected = {
                    showDeleteDialog = true
                }
            )
        },
        floatingActionButton = {
            if (!state.isSelectionModeEnabled) {
                AddFab(
                    onClick = { onNavigate(null) },
                    shouldShow = !lazyColumnState.isScrollInProgress
                )
            }
        }
    ) { innerPadding ->


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }, sheetState = sheetState
            ) {
                NewCategorySheetContent(onAddNewCategory = {
                    onAction(NotesScreenActions.AddNewCategory(it))
                    scope.launch { sheetState.hide() }
                })
            }
        }

        if (showDeleteDialog) {
            DeleteConfirmationDialog(onConfirm = {
                onAction(NotesScreenActions.OnBulkDelete)
            }, onDismiss = {
                showDeleteDialog = false
            }
            )
        }
        NoteListContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = state,
            animatedContentScope = animatedContentScope,
            sharedElementTransitionScope = sharedElementTransitionScope,
            lazyColumnState = lazyColumnState,
            onNavigate = onNavigate,
            onAddNewCategoryClick = { showBottomSheet = true },
            onCategorySelected = { onAction(NotesScreenActions.SelectCategory(it)) },
            onNoteSelection = { onAction(NotesScreenActions.OnToggleNoteSelection(it)) },
            isSelectionEnabled = state.isSelectionModeEnabled,
            onToggleSelectionMode = { onAction(NotesScreenActions.OnToggleSelectionMode) }
        )
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteListContent(
    modifier: Modifier = Modifier,
    state: NoteListState,
    lazyColumnState: LazyListState = rememberLazyListState(),
    onNavigate: (Int?) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedElementTransitionScope: SharedTransitionScope,
    onAddNewCategoryClick: () -> Unit,
    onCategorySelected: (Int?) -> Unit,
    onNoteSelection: (Note) -> Unit,
    isSelectionEnabled: Boolean,
    onToggleSelectionMode: () -> Unit
) {

    if (!state.isLoading && state.notes.isEmpty()) EmptyContent(message = "No Notes Found")


    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = lazyColumnState
    ) {
        if (state is NotesScreenState) {
            item {
                CategoryBar(
                    categories = state.categories,
                    currentSelectedCategoryId = state.selectedCategoryId,
                    onCategorySelected = { onCategorySelected(it) },
                    onAddNewCategoryClick = { onAddNewCategoryClick() },
                    isLoading = state.isLoading
                )
            }
        }

        items(state.notes, key = { note -> note.id }) { note ->

            NoteListItem(
                modifier = Modifier.animateItem(),
                note = note,
                onItemClicked = { id -> onNavigate(id) },
                sharedElementTransitionScope = sharedElementTransitionScope,
                animatedContentScope = animatedContentScope,
                onNoteSelect = { onNoteSelection(it) },
                onTriggerSelection = onToggleSelectionMode,
                isSelectionEnabled = isSelectionEnabled,
                isSelected = (state as NotesScreenState).selectedNotes.contains(note)
            )
        }
    }

}





