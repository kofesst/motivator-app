package me.kofesst.android.motivatorapp.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.flow.Flow
import me.kofesst.android.motivatorapp.R
import me.kofesst.android.motivatorapp.domain.models.results.AuthResult
import me.kofesst.android.motivatorapp.presentation.LocalAppState
import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.utils.AppText
import me.kofesst.android.motivatorapp.presentation.utils.UiText
import me.kofesst.android.motivatorapp.presentation.utils.errorMessage
import me.kofesst.android.motivatorapp.ui.components.*

class AuthScreen(
    routeName: String,
) : Screen<AuthViewModel>(
    routeName = routeName
) {
    override val viewModelProducer:
            @Composable (NavHostController, NavBackStackEntry) -> AuthViewModel
        get() = { _, _ -> hiltViewModel() }

    override val content:
            @Composable BoxScope.(NavBackStackEntry, AuthViewModel, Modifier) -> Unit
        get() = { _, viewModel, modifier ->
            LaunchedEffect(Unit) {
                viewModel.tryRestoreSession()
            }

            val screenState by viewModel.screenState
            val formState = viewModel.formState
            ScreenContent(
                screenState = screenState,
                onScreenStateToggle = {
                    viewModel.toggleScreenState()
                },
                formState = formState,
                onFormAction = { action ->
                    viewModel.onFormAction(action)
                },
                modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp)
            )

            val context = LocalContext.current
            val appState = LocalAppState.current
            FormResultListener(
                result = viewModel.formResult,
                onSuccess = {
                    appState.navController.navigate(
                        route = Feed.routeName
                    ) {
                        popUpTo(this@AuthScreen.routeName) {
                            inclusive = true
                        }
                    }
                },
                onFailed = {
                    appState.showSnackbar(
                        message = it.errorMessage(context = context)
                    )
                }
            )

            LoadingHandler(
                viewModel = viewModel
            )

            val sessionCheckState by viewModel.sessionCheckState
            AnimatedVisibility(
                visible = sessionCheckState,
                enter = EnterTransition.None,
                exit = fadeOut(
                    animationSpec = keyframes {
                        this.durationMillis = 1000
                    }
                )
            ) {
                AuthSessionSplashScreen()
            }
        }

    @Composable
    private fun AuthSessionSplashScreen() {
        LottieMessage(
            lottieRes = R.raw.auth_lottie,
            message = AppText.checkingForSession(),
            shouldFillBackground = true
        )
    }

    @Composable
    private fun ScreenContent(
        formState: AuthFormState,
        onFormAction: (AuthFormAction) -> Unit,
        screenState: AuthScreenState,
        onScreenStateToggle: () -> Unit,
        modifier: Modifier = Modifier,
        spacing: Dp = 10.dp,
    ) {
        Box(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxSize()
            ) {
                AuthGreetings(
                    modifier = Modifier.fillMaxWidth()
                )
                AuthLottie(
                    modifier = Modifier.weight(0.35f)
                )
                AuthForm(
                    formState = formState,
                    onFormAction = onFormAction,
                    screenState = screenState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                )
                AuthFormActions(
                    screenState = screenState,
                    onScreenStateToggle = onScreenStateToggle,
                    onSubmit = {
                        onFormAction(
                            AuthFormAction.Submit
                        )
                    }
                )
            }
        }
    }

    @Composable
    private fun AuthGreetings(
        modifier: Modifier = Modifier,
        blocksSpacing: Dp = 10.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(blocksSpacing),
            modifier = modifier
        ) {
            Text(
                text = AppText.authWelcomeText(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = AppText.authSubtitleText(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    @Composable
    private fun AuthLottie(
        modifier: Modifier = Modifier,
    ) {
        val lottieComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.auth_lottie)
        )
        val progress by animateLottieCompositionAsState(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )
        Box(
            modifier = modifier
        ) {
            LottieAnimation(
                composition = lottieComposition,
                progress = { progress },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Composable
    private fun AuthForm(
        formState: AuthFormState,
        onFormAction: (AuthFormAction) -> Unit,
        screenState: AuthScreenState,
        modifier: Modifier = Modifier,
        formSpacing: Dp = 10.dp,
    ) {
        Box(
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(formSpacing),
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                EmailField(
                    email = formState.email,
                    errorMessage = formState.emailError,
                    onEmailChange = {
                        onFormAction(
                            AuthFormAction.EmailChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    password = formState.password,
                    errorMessage = formState.passwordError,
                    onPasswordChange = {
                        onFormAction(
                            AuthFormAction.PasswordChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                if (screenState == AuthScreenState.SignUp) {
                    SignUpFields(
                        firstName = formState.firstName,
                        firstNameError = formState.firstNameError,
                        onFirstNameChange = {
                            onFormAction(
                                AuthFormAction.FirstNameChanged(it)
                            )
                        },
                        lastName = formState.lastName,
                        lastNameError = formState.lastNameError,
                        onLastNameChange = {
                            onFormAction(
                                AuthFormAction.LastNameChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                RememberSessionCheckBox(
                    checked = formState.rememberSession,
                    onCheck = {
                        onFormAction(
                            AuthFormAction.RememberSessionChanged(it)
                        )
                    },
                    label = AppText.Action.rememberSession(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun AuthFormActions(
        screenState: AuthScreenState,
        onScreenStateToggle: () -> Unit,
        onSubmit: () -> Unit,
        modifier: Modifier = Modifier,
        actionsSpacing: Dp = 20.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            AuthFormSubmitButton(
                screenState = screenState,
                onSubmit = onSubmit,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(actionsSpacing)
            )
            ToggleScreenStateAction(
                screenState = screenState,
                onScreenStateToggle = onScreenStateToggle,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    private fun EmailField(
        email: String,
        errorMessage: UiText?,
        onEmailChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = email,
            onValueChange = onEmailChange,
            errorMessage = errorMessage?.invoke(),
            textStyle = MaterialTheme.typography.bodyLarge,
            label = AppText.Label.email(),
            modifier = modifier
        )
    }

    @Composable
    private fun PasswordField(
        password: String,
        errorMessage: UiText?,
        onPasswordChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            errorMessage = errorMessage?.invoke(),
            textStyle = MaterialTheme.typography.bodyLarge,
            label = AppText.Label.password(),
            modifier = modifier
        )
    }

    @Composable
    private fun SignUpFields(
        firstName: String,
        firstNameError: UiText?,
        onFirstNameChange: (String) -> Unit,
        lastName: String,
        lastNameError: UiText?,
        onLastNameChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        FirstNameField(
            firstName = firstName,
            firstNameError = firstNameError,
            onFirstNameChange = onFirstNameChange,
            modifier = modifier
        )
        LastNameField(
            lastName = lastName,
            lastNameError = lastNameError,
            onLastNameChange = onLastNameChange,
            modifier = modifier
        )
    }

    @Composable
    private fun FirstNameField(
        firstName: String,
        firstNameError: UiText?,
        onFirstNameChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            errorMessage = firstNameError?.invoke(),
            textStyle = MaterialTheme.typography.bodyLarge,
            label = AppText.Label.firstName(),
            modifier = modifier
        )
    }

    @Composable
    private fun LastNameField(
        lastName: String,
        lastNameError: UiText?,
        onLastNameChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            errorMessage = lastNameError?.invoke(),
            textStyle = MaterialTheme.typography.bodyLarge,
            label = AppText.Label.lastName(),
            modifier = modifier
        )
    }

    @Composable
    fun RememberSessionCheckBox(
        checked: Boolean,
        onCheck: (Boolean) -> Unit,
        label: String,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .clickable(
                    indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onCheck(!checked) }
                )
                .requiredHeight(ButtonDefaults.MinHeight)
                .padding(4.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(Modifier.size(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    @Composable
    fun AuthFormSubmitButton(
        screenState: AuthScreenState,
        onSubmit: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AppButton(
            uiText = when (screenState) {
                AuthScreenState.SignIn -> {
                    AppText.Action.signIn
                }
                AuthScreenState.SignUp -> {
                    AppText.Action.signUp
                }
            },
            onClick = onSubmit,
            modifier = modifier
        )
    }

    @Composable
    private fun ToggleScreenStateAction(
        screenState: AuthScreenState,
        onScreenStateToggle: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = when (screenState) {
                    AuthScreenState.SignIn -> {
                        AppText.hasNotProfileText()
                    }
                    AuthScreenState.SignUp -> {
                        AppText.alreadyHasProfile()
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )
            AppTextButton(
                uiText = when (screenState) {
                    AuthScreenState.SignIn -> {
                        AppText.Action.signUp
                    }
                    AuthScreenState.SignUp -> {
                        AppText.Action.signIn
                    }
                },
                onClick = onScreenStateToggle
            )
        }
    }

    @Composable
    private fun FormResultListener(
        result: Flow<AuthResult>,
        onSuccess: (AuthResult.Success) -> Unit,
        onFailed: (AuthResult.Failed) -> Unit,
    ) {
        LaunchedEffect(Unit) {
            result.collect {
                when (it) {
                    is AuthResult.Success -> {
                        onSuccess(it)
                    }
                    is AuthResult.Failed -> {
                        onFailed(it)
                    }
                }
            }
        }
    }
}