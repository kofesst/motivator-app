package me.kofesst.android.motivatorapp.presentation.post.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.screen.ScreenConstants
import me.kofesst.android.motivatorapp.presentation.screen.TopBarSettings
import me.kofesst.android.motivatorapp.presentation.screen.withArgs
import me.kofesst.android.motivatorapp.presentation.utils.AppText
import me.kofesst.android.motivatorapp.presentation.utils.formatDate
import me.kofesst.android.motivatorapp.ui.components.AppButton
import me.kofesst.android.motivatorapp.ui.components.LoadingStateHandler
import me.kofesst.android.motivatorapp.ui.components.PostAuthorRow
import java.util.*

class PostOverviewScreen(
    routeName: String,
) : Screen<PostOverviewViewModel>(
    routeName = routeName,
    args = listOf(
        navArgument(name = ScreenConstants.ID_ARG_NAME) {
            type = NavType.StringType
            nullable = false
        }
    ),
    topBarSettings = TopBarSettings(
        visible = true,
        hasBackButton = true,
        title = AppText.Title.postOverview
    )
) {
    override val viewModelProducer: @Composable (NavHostController, NavBackStackEntry) -> PostOverviewViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, PostOverviewViewModel, Modifier) -> Unit
        get() = { backStack, viewModel, modifier ->
            val appState = LocalAppState.current
            val postId = getStringArg(
                name = ScreenConstants.ID_ARG_NAME,
                backStackEntry = backStack
            )

            val context = LocalContext.current
            LaunchedEffect(postId) {
                if (postId.isBlank()) {
                    appState.showSnackbar(
                        message = AppText.Snackbar.cannotGetPostId(context)
                    )
                    appState.navController.navigateUp()
                } else {
                    viewModel.loadPost(
                        id = postId
                    )
                }
            }

            val postState by viewModel.postState
            val ownedPost by viewModel.ownedPostState
            LoadingStateHandler(
                state = postState,
                content = { postWithAuthor ->
                    ScreenContent(
                        postWithAuthor = postWithAuthor,
                        ownedPost = ownedPost,
                        onCompleteClick = {
                            viewModel.completePost(postWithAuthor.post) {
                                appState.showSnackbar(
                                    message = AppText.Snackbar.postCompleted(context)
                                )
                                appState.navController.navigateUp()
                            }
                        },
                        onAuthorClick = { profileId ->
                            appState.navController.navigate(
                                route = ProfileOverview.withArgs(
                                    ScreenConstants.ID_ARG_NAME to profileId
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                },
                modifier = modifier.verticalScroll(rememberScrollState())
            )
        }

    @Composable
    private fun ScreenContent(
        postWithAuthor: MotivationPostWithAuthor,
        ownedPost: Boolean,
        onCompleteClick: () -> Unit,
        onAuthorClick: (String) -> Unit,
        modifier: Modifier = Modifier,
        spacing: Dp = 15.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier
        ) {
            Text(
                text = postWithAuthor.post.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = postWithAuthor.post.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            PostDetails(
                authorName = postWithAuthor.author.fullName,
                authorAvatarUrl = postWithAuthor.author.appAvatarUrl,
                onAuthorClick = {
                    onAuthorClick(postWithAuthor.author.id)
                },
                expired = postWithAuthor.post.expired,
                completed = postWithAuthor.post.completed,
                deadline = postWithAuthor.post.deadline,
                modifier = Modifier.fillMaxWidth()
            )
            if (ownedPost && !postWithAuthor.post.completed && !postWithAuthor.post.expired) {
                CompletePostButton(
                    post = postWithAuthor.post,
                    onClick = onCompleteClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun PostDetails(
        authorName: String,
        authorAvatarUrl: String,
        onAuthorClick: () -> Unit,
        expired: Boolean,
        completed: Boolean,
        deadline: Date,
        modifier: Modifier = Modifier,
    ) {
        Text(
            text = if (expired) {
                AppText.PostStatus.expired()
            } else if (completed) {
                AppText.PostStatus.completed()
            } else {
                AppText.PostStatus.waiting(deadline.formatDate())
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
        PostAuthorRow(
            authorName = authorName,
            authorAvatarUrl = authorAvatarUrl,
            onClick = onAuthorClick,
            modifier = modifier
        )
    }

    @Composable
    private fun CompletePostButton(
        post: MotivationPost,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppButton(
            uiText = AppText.Action.completePost,
            onClick = onClick,
            modifier = modifier,
            enabled = !post.expired
        )
    }
}