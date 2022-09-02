package me.kofesst.android.motivatorapp.presentation.profile.edit

import android.Manifest
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.Flow
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import me.kofesst.android.motivatorapp.presentation.LocalActivity
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.screen.ScreenConstants
import me.kofesst.android.motivatorapp.presentation.screen.TopBarSettings
import me.kofesst.android.motivatorapp.presentation.utils.*
import me.kofesst.android.motivatorapp.ui.components.*

class EditProfileScreen(
    routeName: String,
) : Screen<EditProfileViewModel>(
    routeName = routeName,
    args = listOf(
        navArgument(name = ScreenConstants.ID_ARG_NAME) {
            type = NavType.StringType
            nullable = false
        }
    ),
    topBarSettings = TopBarSettings(
        visible = true,
        title = AppText.Title.editProfile,
        hasBackButton = true
    )
) {
    override val viewModelProducer:
            @Composable (NavHostController, NavBackStackEntry) -> EditProfileViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, EditProfileViewModel, Modifier) -> Unit
        get() = { backStack, viewModel, modifier ->
            val appState = LocalAppState.current
            LaunchedEffect(Unit) {
                val profileId = getStringArg(
                    name = ScreenConstants.ID_ARG_NAME,
                    backStackEntry = backStack
                )
                viewModel.loadProfile(profileId)
            }

            val profileState by viewModel.profileState
            val formState = viewModel.formState
            LoadingStateHandler(
                state = profileState,
                content = {
                    ScreenContent(
                        formState = formState,
                        onFormAction = { action ->
                            viewModel.onFormAction(action)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .align(Alignment.Center)
                    )
                },
                modifier = modifier
            )

            LoadingHandler(
                viewModel = viewModel
            )

            FormResultListener(
                result = viewModel.formResult
            ) {
                appState.navController.navigateUp()
            }
        }

    @Composable
    fun ScreenContent(
        formState: ProfileFormState,
        onFormAction: (ProfileFormAction) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            FirstNameField(
                firstName = formState.firstName,
                errorMessage = formState.firstNameError,
                onFirstNameChanged = {
                    onFormAction(
                        ProfileFormAction.FirstNameChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            LastNameField(
                lastName = formState.lastName,
                errorMessage = formState.lastNameError,
                onLastNameChanged = {
                    onFormAction(
                        ProfileFormAction.LastNameChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            DividerWithText(
                text = AppText.Label.profileAvatar(),
                modifier = Modifier.padding(vertical = 10.dp)
            )
            AvatarFilePicker(
                avatarContent = formState.avatarContent,
                onAvatarChange = {
                    onFormAction(
                        ProfileFormAction.AvatarChanged(it)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Divider(
                modifier = Modifier.padding(vertical = 10.dp)
            )
            AppButton(
                uiText = AppText.Action.saveProfile,
                onClick = {
                    onFormAction(
                        ProfileFormAction.Submit
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun FirstNameField(
        firstName: String,
        errorMessage: UiText?,
        onFirstNameChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = firstName,
            errorMessage = errorMessage?.invoke(),
            onValueChange = onFirstNameChanged,
            leadingIcon = Icons.Outlined.Person,
            label = AppText.Label.firstName(),
            maxCounter = AppConstants.Profile.NAME_LENGTH_RANGE.last,
            modifier = modifier
        )
    }

    @Composable
    private fun LastNameField(
        lastName: String,
        errorMessage: UiText?,
        onLastNameChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = lastName,
            errorMessage = errorMessage?.invoke(),
            onValueChange = onLastNameChanged,
            leadingIcon = Icons.Outlined.Person,
            label = AppText.Label.lastName(),
            maxCounter = AppConstants.Profile.NAME_LENGTH_RANGE.last,
            modifier = modifier
        )
    }

    @Composable
    private fun AvatarFilePicker(
        avatarContent: ByteArray,
        onAvatarChange: (ByteArray) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current
        val resolver = LocalActivity.current.contentResolver
        val fileSelectRequester = rememberFilePicker {
            val contentUri = it.data ?: return@rememberFilePicker
            val inputStream = resolver.openInputStream(contentUri) ?: return@rememberFilePicker
            val fileContent = inputStream.readBytes()
            onAvatarChange(fileContent)
        }

        val appState = LocalAppState.current
        val storagePermissionRequester = rememberPermissionLauncher(
            onDismiss = {
                appState.showSnackbar(
                    message = AppText.Snackbar.needStoragePermission(context)
                )
            }
        ) {
            fileSelectRequester.launch(imagePickerIntent)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            if (avatarContent.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val avatarBitmap = BitmapFactory.decodeByteArray(
                        avatarContent, 0, avatarContent.size
                    ).asImageBitmap()

                    Image(
                        bitmap = avatarBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )
                    Image(
                        bitmap = avatarBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
                AppTextButton(
                    uiText = AppText.Action.removeAvatar,
                    onClick = {
                        onAvatarChange(byteArrayOf())
                    }
                )
            }
            ExtendedFloatingActionButton(
                onClick = {
                    if (hasStoragePermission) {
                        fileSelectRequester.launch(imagePickerIntent)
                    } else {
                        storagePermissionRequester.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                },
                text = {
                    Text(
                        text = AppText.Action.uploadAvatar(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.FileUpload,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun FormResultListener(
        result: Flow<ProfileFormResult>,
        onSuccess: suspend (ProfileFormResult.Success) -> Unit = {},
    ) {
        LaunchedEffect(Unit) {
            result.collect {
                when (it) {
                    is ProfileFormResult.Success -> {
                        onSuccess(it)
                    }
                }
            }
        }
    }
}