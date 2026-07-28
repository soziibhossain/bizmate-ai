package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.ContentRepository
import com.example.ui.MainViewModel
import com.example.ui.components.BottomBar
import com.example.ui.components.TopBar
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.history.HistoryScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.tools.ToolDetailScreen
import com.example.ui.screens.tools.ToolsScreen
import com.example.ui.theme.BizMateAITheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BizMateAITheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "home"

                val userPref by viewModel.userPreferences.collectAsState()
                val savedList by viewModel.savedContents.collectAsState()
                val saveNotification by viewModel.saveNotification.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(saveNotification) {
                    saveNotification?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearSaveNotification()
                    }
                }

                val showBottomBar = currentRoute in listOf("home", "tools", "history", "profile")
                val showTopBar = currentRoute in listOf("home", "tools", "history", "profile")

                val startDestination = if (userPref?.isOnboardingCompleted == true) "home" else "onboarding"

                Scaffold(
                    topBar = {
                        if (showTopBar) {
                            TopBar(
                                dailyUsageCount = userPref?.dailyAiCount ?: 0,
                                maxLimit = ContentRepository.MAX_DAILY_AI_LIMIT,
                                preferredLanguage = userPref?.preferredLanguage ?: "English",
                                onLanguageClick = {
                                    val currentLang = userPref?.preferredLanguage ?: "English"
                                    val newLang = if (currentLang == "English") "বাংলা" else "English"
                                    viewModel.updateProfile(
                                        userPref?.userName ?: "Tanvir",
                                        userPref?.businessType ?: "Fashion & Apparel",
                                        newLang
                                    )
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            BottomBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("onboarding") {
                            OnboardingScreen(
                                onFinishOnboarding = { bType, lang ->
                                    viewModel.completeOnboarding(bType, lang)
                                    navController.navigate("home") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("auth") {
                            AuthScreen(
                                onLoginSuccess = { email, name ->
                                    viewModel.loginUser(email, name)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                userPreferences = userPref,
                                recentContents = savedList,
                                onSelectTool = { toolId ->
                                    viewModel.resetGenerationState()
                                    navController.navigate("tool_detail/$toolId")
                                },
                                onViewAllHistory = {
                                    navController.navigate("history")
                                }
                            )
                        }

                        composable("tools") {
                            ToolsScreen(
                                userPreferences = userPref,
                                onSelectTool = { toolId ->
                                    viewModel.resetGenerationState()
                                    navController.navigate("tool_detail/$toolId")
                                }
                            )
                        }

                        composable(
                            route = "tool_detail/{toolType}",
                            arguments = listOf(navArgument("toolType") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val toolType = backStackEntry.arguments?.getString("toolType") ?: "SOCIAL_POST"
                            ToolDetailScreen(
                                toolType = toolType,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                viewModel = viewModel,
                                savedList = savedList
                            )
                        }

                        composable("profile") {
                            ProfileScreen(
                                userPreferences = userPref,
                                viewModel = viewModel,
                                onNavigateToAuth = { navController.navigate("auth") }
                            )
                        }
                    }
                }
            }
        }
    }
}
