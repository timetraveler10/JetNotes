package com.hussein.jetnotes.presentation.main_app_destinations.main_screen

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
import com.hussein.jetnotes.presentation.components.CategoryBar
import com.hussein.jetnotes.presentation.main_app_destinations.main_screen.components.AddFab
import com.hussein.jetnotes.presentation.main_app_destinations.shared.AddNewCategorySheetContent
import com.hussein.jetnotes.presentation.main_app_destinations.main_screen.components.MainScreenTopAppBar
import com.hussein.jetnotes.presentation.main_app_destinations.main_screen.components.NoteListItem
import com.hussein.jetnotes.presentation.main_app_destinations.util.ObserveAsEvents
import com.hussein.jetnotes.presentation.main_app_destinations.util.SnackbarController
import com.hussein.jetnotes.presentation.util.EmptyContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onAction: (MainScreenActions) -> Unit,
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


    LaunchedEffect(state.selectedCategoryId) {
        if (!state.isLoading) {
            onAction(MainScreenActions.LoadData(state.selectedCategoryId))
        }
        onAction(MainScreenActions.LoadCategories)
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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainScreenTopAppBar(
                scrollBehavior = scrollBehavior,
                onNavigateToPasscodeSetup = {
                    onNavigateToPasscodeSetup()
                }, onNavigateToSettings = onNavigateToSettings
            )
        },
        floatingActionButton = {
            AddFab(
                onClick = { onNavigate(null) },
                shouldShow = !lazyColumnState.isScrollInProgress
            )
        }
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
                AddNewCategorySheetContent(onAddNewCategory = {
                    onAction(MainScreenActions.AddNewCategory(it))
                    scope.launch { sheetState.hide() }
                })
            }
        }


        MainScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = state,
            onAddNewCategoryClick = { showBottomSheet = true },
            onCategorySelected = { categoryId ->
                onAction(MainScreenActions.SelectCategory(categoryId = categoryId))
            },
            animatedContentScope = animatedContentScope,
            sharedElementTransitionScope = sharedElementTransitionScope,
            lazyColumnState = lazyColumnState,
            onNavigate = onNavigate
        )


//        with(sharedElementTransitionScope) {
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(4.dp),
//                state = lazyColumnState
//            ) {
//                item {
//                    CategoryBar(
//                        categories = state.categories,
//                        currentSelectedCategoryId = state.selectedCategoryId,
//                        onCategorySelected = {
//                            onAction(MainScreenActions.SelectCategory(it))
//                        },
//                        onAddNewCategoryClick = { showBottomSheet = true },
//                        isLoading = state.isLoading
//                    )
//                }
//                items(state.notes, key = { note -> note.id }) { note ->
//                    NoteListItem(
//                        modifier = Modifier
//                            .sharedElement(
//                                sharedContentState = rememberSharedContentState("note-${note.id}"),
//                                animatedVisibilityScope = animatedContentScope,
//                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize
//                            )
//                            .animateItem(),
//                        note = note,
//                        onItemClicked = { id -> onNavigate(id) },
//                        animatedContentScope = animatedContentScope,
//                        sharedElementTransitionScope = sharedElementTransitionScope
//                    )
//                }
//            }
//        }

    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    state: MainScreenState,
    lazyColumnState: LazyListState = rememberLazyListState(),
    onAddNewCategoryClick: () -> Unit,
    onCategorySelected: (categoryId: Int?) -> Unit,
    onNavigate: (Int?) -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedElementTransitionScope: SharedTransitionScope
) {
    with(sharedElementTransitionScope) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            state = lazyColumnState
        ) {
            item {
                CategoryBar(
                    categories = state.categories,
                    currentSelectedCategoryId = state.selectedCategoryId,
                    onCategorySelected = onCategorySelected,
                    onAddNewCategoryClick = onAddNewCategoryClick,
                    isLoading = state.isLoading
                )
            }
            items(state.notes, key = { note -> note.id }) { note ->
                NoteListItem(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("note-${note.id}"),
                            animatedVisibilityScope = animatedContentScope,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize
                        )
                        .animateItem(),
                    note = note,
                    onItemClicked = { id -> onNavigate(id) },
                )
            }
        }

    }

}




