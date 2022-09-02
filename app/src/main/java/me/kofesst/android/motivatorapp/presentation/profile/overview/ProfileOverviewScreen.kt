package me.kofesst.android.motivatorapp.presentation.profile.overview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.UserProfileWithPosts
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.*
import me.kofesst.android.motivatorapp.presentation.utils.*
import me.kofesst.android.motivatorapp.ui.components.AppButton
import me.kofesst.android.motivatorapp.ui.components.DividerWithText
import me.kofesst.android.motivatorapp.ui.components.LoadingStateHandler
import me.kofesst.android.motivatorapp.ui.components.PostsList

class ProfileOverviewScreen(
    routeName: String,
) : Screen<ProfileOverviewViewModel>(
    routeName = routeName,
    args = listOf(
        navArgument(name = ScreenConstants.ID_ARG_NAME) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    ),
    topBarSettings = TopBarSettings(
        visible = true,
        title = AppText.Title.profileOverview
    ),
    bottomBarSettings = BottomBarSettings(
        visible = true,
        icon = Icons.Outlined.AccountCircle,
        title = AppText.Title.profileOverview
    )
) {
    override val viewModelProducer:
            @Composable (NavHostController, NavBackStackEntry) -> ProfileOverviewViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, ProfileOverviewViewModel, Modifier) -> Unit
        get() = { backStack, viewModel, modifier ->
            val profileId = getStringArg(
                name = ScreenConstants.ID_ARG_NAME,
                backStackEntry = backStack,
            )
            LaunchedEffect(Unit) {
                viewModel.loadProfile(profileId)
            }

            val appState = LocalAppState.current
            val profileState by viewModel.profileState
            val isSelfProfile by viewModel.isSelfProfile
            LoadingStateHandler(
                state = profileState,
                content = { profileWithPosts ->
                    ScreenContent(
                        profileWithPosts = profileWithPosts,
                        isSelfProfile = isSelfProfile,
                        onEditClick = {
                            appState.navController.navigate(
                                route = EditProfile.withArgs(
                                    ScreenConstants.ID_ARG_NAME to profileWithPosts.profile.id
                                )
                            )
                        },
                        onPostClick = { post ->
                            appState.navController.navigate(
                                route = PostOverview.withArgs(
                                    ScreenConstants.ID_ARG_NAME to post.id
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                },
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }

    @Composable
    fun ScreenContent(
        profileWithPosts: UserProfileWithPosts,
        isSelfProfile: Boolean,
        onEditClick: () -> Unit,
        onPostClick: (MotivationPost) -> Unit,
        modifier: Modifier = Modifier,
        spacing: Dp = 15.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier
        ) {
            ProfileHeader(
                fullName = profileWithPosts.profile.fullName,
                avatarUrl = profileWithPosts.profile.appAvatarUrl,
                postsCount = profileWithPosts.posts.size,
                rating = profileWithPosts.userRating,
                modifier = Modifier.fillMaxWidth()
            )
            if (isSelfProfile) {
                EditProfileButton(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            DividerWithText(
                text = AppText.profilePosts()
            )
            ProfilePosts(
                profileWithPosts = profileWithPosts,
                onItemClick = onPostClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun ProfileHeader(
        fullName: String,
        avatarUrl: String,
        postsCount: Int,
        rating: Int,
        modifier: Modifier = Modifier,
        spacing: Dp = 15.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier
        ) {
            Text(
                text = fullName,
                style = MaterialTheme.typography.headlineSmall
            )
            ProfileDetailsRow(
                avatarUrl = avatarUrl,
                postsCount = postsCount,
                rating = rating,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun ProfileDetailsRow(
        avatarUrl: String,
        postsCount: Int,
        rating: Int,
        modifier: Modifier = Modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            ProfileAvatar(
                avatarUrl = avatarUrl
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1.0f)
            ) {
                ProfileStatsCounter(
                    text = postsCases,
                    value = postsCount
                )
                ProfileStatsCounter(
                    text = ratingCases,
                    value = rating
                )
            }
        }
    }

    @Composable
    private fun ProfileAvatar(
        avatarUrl: String,
        modifier: Modifier = Modifier,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
    }

    @Composable
    private fun ProfileStatsCounter(
        text: UiWordCases,
        value: Int,
        modifier: Modifier = Modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Text(
                text = value.asCounter(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text.getCase(value),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    @Composable
    private fun EditProfileButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppButton(
            uiText = AppText.Action.editProfile,
            onClick = onClick,
            modifier = modifier
        )
    }

    @Composable
    private fun ProfilePosts(
        profileWithPosts: UserProfileWithPosts,
        onItemClick: (MotivationPost) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        PostsList(
            author = profileWithPosts.profile,
            posts = profileWithPosts.posts,
            onItemClick = { onItemClick(profileWithPosts.posts[it]) },
            modifier = modifier
        )
    }
}