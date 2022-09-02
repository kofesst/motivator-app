@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package me.kofesst.android.motivatorapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.domain.models.UserProfile
import me.kofesst.android.motivatorapp.presentation.utils.AppText
import me.kofesst.android.motivatorapp.presentation.utils.color
import me.kofesst.android.motivatorapp.presentation.utils.formatDate
import me.kofesst.android.motivatorapp.presentation.utils.title
import java.util.*

@Composable
fun PostsList(
    author: UserProfile,
    posts: List<MotivationPost>,
    modifier: Modifier = Modifier,
    itemsSpace: Dp = 20.dp,
    onItemClick: (Int) -> Unit = {},
) {
    MotivationPostsList(
        postsWithAuthorMap = posts.associateWith { author },
        itemsSpace = itemsSpace,
        modifier = modifier,
        onItemClick = onItemClick
    )
}

@Composable
fun PostsWithAuthorList(
    postsWithAuthor: List<MotivationPostWithAuthor>,
    modifier: Modifier = Modifier,
    itemsSpace: Dp = 20.dp,
    onItemClick: (Int) -> Unit = {},
) {
    MotivationPostsList(
        postsWithAuthorMap = postsWithAuthor.associate { it.post to it.author },
        itemsSpace = itemsSpace,
        modifier = modifier,
        onItemClick = onItemClick
    )
}

@Composable
private fun MotivationPostsList(
    postsWithAuthorMap: Map<MotivationPost, UserProfile>,
    itemsSpace: Dp,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (postsWithAuthorMap.isEmpty()) {
        ErrorPanel(
            text = AppText.thereIsEmptyText()
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(itemsSpace),
            modifier = modifier
        ) {
            postsWithAuthorMap.keys.forEachIndexed { index, post ->
                val authorProfile = postsWithAuthorMap[post] ?: return@forEachIndexed
                PostItem(
                    item = post,
                    authorName = authorProfile.fullName,
                    authorAvatarUrl = authorProfile.appAvatarUrl,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onItemClick(index) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    item: MotivationPost,
    authorName: String,
    authorAvatarUrl: String,
    modifier: Modifier = Modifier,
    padding: Dp = 14.dp,
    spacing: Dp = 10.dp,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.padding(padding)
        ) {
            PostItemHeader(
                title = item.title,
                difficulty = item.difficulty,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            PostItemDetails(
                authorName = authorName,
                authorAvatarUrl = authorAvatarUrl,
                expired = item.expired,
                completed = item.completed,
                deadline = item.deadline,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PostItemHeader(
    title: String,
    difficulty: MotivationPost.Difficulty,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.0f)
        )
        Text(
            text = difficulty.title().lowercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = difficulty.color
        )
    }
}

@Composable
private fun PostItemDetails(
    authorName: String,
    authorAvatarUrl: String,
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
        style = MaterialTheme.typography.bodyMedium
    )
    PostAuthorRow(
        authorName = authorName,
        authorAvatarUrl = authorAvatarUrl,
        modifier = modifier
    )
}

@Composable
fun PostAuthorRow(
    authorName: String,
    authorAvatarUrl: String,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 24.dp,
    spacing: Dp = 10.dp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(authorAvatarUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
        )
        Text(
            text = authorName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun PostAuthorRow(
    authorName: String,
    authorAvatarUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 24.dp,
    spacing: Dp = 10.dp,
) {
    AppTextButton(
        content = {
            PostAuthorRow(
                authorName = authorName,
                authorAvatarUrl = authorAvatarUrl,
                avatarSize = avatarSize,
                spacing = spacing,
                modifier = Modifier.fillMaxWidth()
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}