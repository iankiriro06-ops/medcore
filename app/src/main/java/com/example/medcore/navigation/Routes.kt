package com.medcore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medcore.app.ui.screens.*

// ── Route Definitions ─────────────────────────────────────────────────────────

object Route {
    const val SPLASH       = "splash"
    const val LOGIN        = "login"
    const val REGISTER     = "register"
    const val HOME         = "home"
    const val SYSTEM_DETAIL = "system/{systemId}"
    const val TOPIC        = "topic/{topicId}"
    const val PROGRESS     = "progress"
    const val SUBSCRIPTION = "subscription"
    const val PROFILE      = "profile"

    fun systemDetail(systemId: String) = "system/$systemId"
    fun topic(topicId: String)         = "topic/$topicId"
}

// ── Bottom Nav Items ──────────────────────────────────────────────────────────

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem("Home",     Route.HOME,         Icons.Filled.Home,         Icons.Outlined.Home),
    BottomNavItem("Systems",  Route.SYSTEM_DETAIL, Icons.Filled.AccountTree, Icons.Outlined.AccountTree),
    BottomNavItem("Progress", Route.PROGRESS,     Icons.Filled.BarChart,     Icons.Outlined.BarChart),
    BottomNavItem("Profile",  Route.PROFILE,      Icons.Filled.Person,       Icons.Outlined.Person),
)

// ── Nav Graph ─────────────────────────────────────────────────────────────────

@Composable
fun AnatomIQNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.SPLASH,
    ) {
        composable(Route.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Route.LOGIN) {
                    popUpTo(Route.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Route.LOGIN) {
            LoginScreen(
                onLoginSuccess   = { navController.navigate(Route.HOME) { popUpTo(Route.LOGIN) { inclusive = true } } },
                onGoToRegister   = { navController.navigate(Route.REGISTER) },
            )
        }

        composable(Route.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Route.HOME) { popUpTo(Route.REGISTER) { inclusive = true } } },
                onGoToLogin       = { navController.popBackStack() },
            )
        }

        composable(Route.HOME) {
            HomeScreen(
                onSystemClick      = { id -> navController.navigate(Route.systemDetail(id)) },
                onProgressClick    = { navController.navigate(Route.PROGRESS) },
                onProfileClick     = { navController.navigate(Route.PROFILE) },
                onSubscribeClick   = { navController.navigate(Route.SUBSCRIPTION) },
            )
        }

        composable(Route.SYSTEM_DETAIL) { backStack ->
            val systemId = backStack.arguments?.getString("systemId") ?: "ms"
            SystemDetailScreen(
                systemId        = systemId,
                onBack          = { navController.popBackStack() },
                onTopicClick    = { id -> navController.navigate(Route.topic(id)) },
                onSubscribeClick = { navController.navigate(Route.SUBSCRIPTION) },
            )
        }

        composable(Route.TOPIC) { backStack ->
            val topicId = backStack.arguments?.getString("topicId") ?: ""
            TopicScreen(
                topicId  = topicId,
                onBack   = { navController.popBackStack() },
            )
        }

        composable(Route.PROGRESS) {
            ProgressScreen(
                onBack           = { navController.popBackStack() },
                onSystemClick    = { id -> navController.navigate(Route.systemDetail(id)) },
            )
        }

        composable(Route.SUBSCRIPTION) {
            SubscriptionScreen(
                onBack     = { navController.popBackStack() },
                onSuccess  = { navController.popBackStack() },
            )
        }

        composable(Route.PROFILE) {
            ProfileScreen(
                onBack           = { navController.popBackStack() },
                onSubscribeClick = { navController.navigate(Route.SUBSCRIPTION) },
                onSignOut        = {
                    navController.navigate(Route.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}

