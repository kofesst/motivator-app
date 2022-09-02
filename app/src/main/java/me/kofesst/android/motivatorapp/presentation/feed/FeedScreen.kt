package me.kofesst.android.motivatorapp.presentation.feed

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.*
import me.kofesst.android.motivatorapp.presentation.utils.AppText
import me.kofesst.android.motivatorapp.presentation.utils.LoadingState
import me.kofesst.android.motivatorapp.ui.components.LoadingStateHandler
import me.kofesst.android.motivatorapp.ui.components.PostsWithAuthorList

class FeedScreen(
    routeName: String,
) : Screen<FeedViewModel>(
    routeName = routeName,
    topBarSettings = TopBarSettings(
        visible = true,
        title = AppText.Title.feed
    ),
    bottomBarSettings = BottomBarSettings(
        visible = true,
        icon = Icons.Outlined.Feed,
        title = AppText.Title.feed
    )
) {
    override val viewModelProducer:
            @Composable (NavHostController, NavBackStackEntry) -> FeedViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, FeedViewModel, Modifier) -> Unit
        get() = { _, viewModel, modifier ->
            val appState = LocalAppState.current
            val feedState by viewModel.feedState

            LaunchedEffect(Unit) {
                if (feedState !is LoadingState.Loaded) {
                    viewModel.loadFeed()
                }
            }

            LoadingStateHandler(
                state = feedState,
                content = { posts ->
                    PostsFeed(
                        posts = posts,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        onItemClick = { postWithAuthor ->
                            appState.navController.navigate(
                                PostOverview.withArgs(
                                    ScreenConstants.ID_ARG_NAME to postWithAuthor.post.id
                                )
                            )
                        }
                    )
                },
                modifier = modifier.verticalScroll(rememberScrollState())
            )
        }

    @Composable
    private fun PostsFeed(
        posts: List<MotivationPostWithAuthor>,
        modifier: Modifier = Modifier,
        onItemClick: (MotivationPostWithAuthor) -> Unit = {},
    ) {
        PostsWithAuthorList(
            postsWithAuthor = posts,
            modifier = modifier,
            onItemClick = { onItemClick(posts[it]) }
        )
    }
}