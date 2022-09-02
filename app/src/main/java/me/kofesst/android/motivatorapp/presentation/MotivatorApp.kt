@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package me.kofesst.android.motivatorapp.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.screen.route
import me.kofesst.android.motivatorapp.presentation.screen.withArgs
import me.kofesst.android.motivatorapp.presentation.utils.AppText

val LocalAppState = compositionLocalOf<AppState> {
    error("App state didn't initialize")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotivatorApp() {
    val viewModel = hiltViewModel<MainViewModel>()
    val appState = LocalAppState.current
    val navController = appState.navController
    val topBarState = remember { appState.topBarState }
    val bottomBarState = remember { appState.bottomBarState }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            AppTopBar(
                state = topBarState,
                navController = navController,
                currentScreenRoute = currentRoute,
                onClearSessionClick = {
                    viewModel.clearSession {
                        navController.navigate(
                            route = Screen.Auth.routeName
                        ) {
                            if (currentRoute != null) {
                                popUpTo(currentRoute) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                state = bottomBarState,
                currentScreenRoute = currentRoute,
                navController = navController
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState
            )
        }
    ) {
        ScreensNavHost(
            navController = navController,
            padding = it
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    state: AppState.TopBarState,
    navController: NavController,
    currentScreenRoute: String?,
    onClearSessionClick: () -> Unit,
) {
    val bottomBarScreens = Screen.values.filter { screen ->
        screen.bottomBarSettings.visible
    }
    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            SmallTopAppBar(
                title = {
                    Text(
                        text = state.title(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    if (bottomBarScreens.any { it.equals(currentScreenRoute) }) {
                        IconButton(onClick = onClearSessionClick) {
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = AppText.Action.clearSession()
                            )
                        }
                    }
                    state.actions.forEach {
                        val action = it.onClick()
                        IconButton(onClick = action) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.description()
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (state.hasBackButton) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun AppBottomBar(
    state: AppState.BottomBarState,
    currentScreenRoute: String?,
    navController: NavController,
) {
    val bottomBarScreens = Screen.values.filter { screen ->
        screen.bottomBarSettings.visible
    }
    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        BottomAppBar(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.CreatePost.withArgs()
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null
                    )
                }
            },
            actions = {
                bottomBarScreens.forEach { screen ->
                    val isActive = screen.route == currentScreenRoute
                    NavigationBarItem(
                        selected = isActive,
                        icon = {
                            Icon(
                                imageVector = screen.bottomBarSettings.icon,
                                contentDescription = screen.bottomBarSettings.title()
                            )
                        },
                        label = {
                            Text(
                                text = screen.bottomBarSettings.title(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            if (!isActive) {
                                navController.navigate(screen.route) {
                                    if (currentScreenRoute != null) {
                                        popUpTo(currentScreenRoute) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun ScreensNavHost(
    navController: NavHostController,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.values[0].routeName,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Screen.values.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.args
            ) {
                screen.ScreenContent(
                    navController = navController,
                    backStackEntry = it,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}