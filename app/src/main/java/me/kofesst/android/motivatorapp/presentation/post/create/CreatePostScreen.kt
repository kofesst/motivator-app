package me.kofesst.android.motivatorapp.presentation.post.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.screen.TopBarSettings
import me.kofesst.android.motivatorapp.presentation.utils.AppText
import me.kofesst.android.motivatorapp.presentation.utils.UiText
import me.kofesst.android.motivatorapp.presentation.utils.color
import me.kofesst.android.motivatorapp.presentation.utils.title
import me.kofesst.android.motivatorapp.ui.components.AppButton
import me.kofesst.android.motivatorapp.ui.components.AppTextField

class CreatePostScreen(
    routeName: String,
) : Screen<CreatePostViewModel>(
    routeName = routeName,
    topBarSettings = TopBarSettings(
        visible = true,
        hasBackButton = true,
        title = AppText.Title.createPost
    )
) {
    override val viewModelProducer:
            @Composable (NavHostController, NavBackStackEntry) -> CreatePostViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, CreatePostViewModel, Modifier) -> Unit
        get() = { _, viewModel, modifier ->
            val formState = viewModel.formState
            ScreenContent(
                formState = formState,
                onFormAction = { action ->
                    viewModel.onFormAction(action)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )

            val appState = LocalAppState.current
            FormResultListener(
                result = viewModel.formResult,
                onSuccess = {
                    appState.navController.navigateUp()
                }
            )
        }

    @Composable
    fun ScreenContent(
        formState: PostFormState,
        onFormAction: (PostFormAction) -> Unit,
        modifier: Modifier = Modifier,
        spacing: Dp = 15.dp,
    ) {
        Box(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                PostTitleField(
                    title = formState.title,
                    errorMessage = formState.titleError,
                    onTitleChange = {
                        onFormAction(
                            PostFormAction.TitleChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                PostDescriptionField(
                    description = formState.description,
                    errorMessage = formState.descriptionError,
                    onDescriptionChange = {
                        onFormAction(
                            PostFormAction.DescriptionChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                PostDifficultiesSelection(
                    selected = formState.difficulty,
                    onDifficultyChange = {
                        onFormAction(
                            PostFormAction.DifficultyChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                CreatePostButton(
                    onClick = {
                        onFormAction(
                            PostFormAction.Submit
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun PostTitleField(
        title: String,
        errorMessage: UiText?,
        onTitleChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = title,
            onValueChange = onTitleChange,
            errorMessage = errorMessage?.invoke(),
            leadingIcon = Icons.Outlined.FormatQuote,
            label = AppText.Label.postTitle(),
            maxCounter = AppConstants.Posts.TITLE_LENGTH_RANGE.last,
            modifier = modifier
        )
    }

    @Composable
    private fun PostDescriptionField(
        description: String,
        errorMessage: UiText?,
        onDescriptionChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = description,
            onValueChange = onDescriptionChange,
            singleLine = false,
            errorMessage = errorMessage?.invoke(),
            leadingIcon = Icons.Outlined.Description,
            label = AppText.Label.postDescription(),
            maxCounter = AppConstants.Posts.DESCRIPTION_LENGTH_RANGE.last,
            modifier = modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PostDifficultiesSelection(
        selected: MotivationPost.Difficulty,
        onDifficultyChange: (MotivationPost.Difficulty) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.selectableGroup()
        ) {
            MotivationPost.Difficulty.values().forEach { difficulty ->
                val isSelected = selected == difficulty
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onDifficultyChange(difficulty)
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null
                            )
                        }
                    } else {
                        null
                    },
                    label = {
                        Text(
                            text = difficulty.title(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected) {
                                contentColorFor(backgroundColor = difficulty.color)
                            } else {
                                difficulty.color
                            }
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        labelColor = difficulty.color,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = difficulty.color
                    )
                )
            }
        }
    }

    @Composable
    private fun CreatePostButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppButton(
            uiText = AppText.Action.createPost,
            onClick = onClick,
            modifier = modifier
        )
    }

    @Composable
    private fun FormResultListener(
        result: Flow<PostFormResult>,
        onSuccess: suspend (PostFormResult.Success) -> Unit = {},
    ) {
        LaunchedEffect(Unit) {
            result.collect {
                when (it) {
                    is PostFormResult.Success -> {
                        onSuccess(it)
                    }
                }
            }
        }
    }
}