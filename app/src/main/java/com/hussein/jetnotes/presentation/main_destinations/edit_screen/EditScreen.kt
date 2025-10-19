package com.hussein.jetnotes.presentation.main_app_destinations.edit_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hussein.jetnotes.presentation.components.CategoryBar
import com.hussein.jetnotes.presentation.components.JetAppTextField
import com.hussein.jetnotes.presentation.components.JetRichTextField
import com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.components.EditScreenBottomBar
import com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.components.EditScreenFormattingBottomSheet
import com.hussein.jetnotes.presentation.main_app_destinations.edit_screen.components.EditScreenTopAppBar
import com.hussein.jetnotes.presentation.main_app_destinations.shared.AddNewCategorySheetContent
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    state: EditScreenState,
    onAction: (EditScreenActions) -> Unit,
    id: Int = -1,
    navigateBack: () -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedElementTransitionScope: SharedTransitionScope
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetContentState =
        remember { mutableStateOf<EditScreenSheetState>(EditScreenSheetState.Hidden) }
    val scope = rememberCoroutineScope()

    fun onNavigateBack() {
        onAction(EditScreenActions.SaveNote)
        navigateBack()
    }

    fun dismissSheet() {
        scope.launch {
            sheetState.hide()
            sheetContentState.value = EditScreenSheetState.Hidden
        }
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

    with(sharedElementTransitionScope) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            topBar = {
                EditScreenTopAppBar(onNavigateBack = ::onNavigateBack, onClickOnShowMore = {})
            }, bottomBar = {
                EditScreenBottomBar(
                    modifier = Modifier.fillMaxWidth().imePadding(),
                    onFormatClick = { sheetContentState.value = EditScreenSheetState.Formatting },
                    onAiAssistanceClick = { }
                )
            }) { innerPadding ->

            if (sheetContentState.value != EditScreenSheetState.Hidden) {
                ModalBottomSheet(
                    onDismissRequest = { sheetContentState.value = EditScreenSheetState.Hidden },
                    sheetState = sheetState
                ) {
                    when (sheetContentState.value) {
                        is EditScreenSheetState.AddCategory -> {
                            AddNewCategorySheetContent(
                                onAddNewCategory = { categoryName ->
                                    onAction(EditScreenActions.AddNewCategory(categoryName))
                                    dismissSheet() // Dismiss after action
                                })
                        }

                        is EditScreenSheetState.Formatting -> {
                            EditScreenFormattingBottomSheet(
                                isBoldSelected = state.content.currentSpanStyle.fontWeight == FontWeight.Bold,
                                isItalicSelected = state.content.currentSpanStyle.fontStyle == FontStyle.Italic,
                                isUnderlinedSelected = state.content.currentSpanStyle.textDecoration?.contains(
                                    TextDecoration.Underline
                                ) == true,
                                isLineThroughSelected = state.content.currentSpanStyle.textDecoration?.contains(
                                    TextDecoration.LineThrough
                                ) == true,
                                onSpanOptionSelected = { option ->
                                    onAction(EditScreenActions.ToggleMarkDownSpanOption(option))
                                },
                                isCenterAlignedSelected = state.content.currentParagraphStyle.textAlign == TextAlign.Center,
                                isRightAlignedSelected = state.content.currentParagraphStyle.textAlign == TextAlign.Right,
                                isLeftAlignedSelected = state.content.currentParagraphStyle.textAlign == TextAlign.Left,
                                onMarkDownParagraphOptionSelected = { option ->
                                    onAction(EditScreenActions.ToggleMarkDownParagraphOption(option))
                                },
                                isOrderedListSelected = state.content.isOrderedList,
                                isUnorderedListSelected = state.content.isUnorderedList,
                                onToggleListOption = {
                                    onAction(EditScreenActions.ToggleListOption(it))
                                    dismissSheet() // Dismiss after action
                                }
                            )
                        }

                        EditScreenSheetState.Hidden -> {
                            // This case is handled by the `if` condition, but `when` needs it to be exhaustive.
                        }
                    }
                }
            }

            EditScreenContent(
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("note-${id}"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(horizontal = 4.dp),
                state = state,
                onAction = onAction,
                onAddNewCategoryClick = {
                    sheetContentState.value = EditScreenSheetState.AddCategory
                }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenContent(
    modifier: Modifier = Modifier,
    state: EditScreenState,
    onAction: (EditScreenActions) -> Unit,
    onAddNewCategoryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        // this should be removed
        CategoryBar(
            modifier = Modifier.fillMaxWidth(),
            categories = state.categories,
            currentSelectedCategoryId = state.categoryId,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onCategorySelected = { onAction(EditScreenActions.SelectCategory(it)) },
            isLoading = state.isLoading
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        JetAppTextField(
            modifier = Modifier.fillMaxWidth(),
            text = state.title,
            onValueChange = { onAction(EditScreenActions.UpdateTitle(it)) },
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            placeHolderText = "Title",
            maxLines = 3
        )
        JetRichTextField(
            modifier = Modifier.weight(1f),
            richTextState = state.content,
            color = RichTextEditorDefaults.richTextEditorColors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            ), placeHolderText = "Content",
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
        )
    }
}

