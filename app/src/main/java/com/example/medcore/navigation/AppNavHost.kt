package com.medcore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.medcore.app.ui.screens.HomeScreen
import com.medcore.app.ui.screens.LoginScreen
import com.example.medcore.ui.theme.screens.registerscreen.RegisterScreen
import com.example.medcore.ui.theme.screens.splashscreen.SplashScreen
import com.example.medcore.ui.theme.screens.subscriptionScreen.SubscriptionScreen
import com.example.medcore.ui.theme.screens.systemdetail.SystemDetailScreen
import com.example.medcore.ui.theme.screens.topicScreen.TopicScreen
import com.example.medcore.ui.theme.screens.quizScreen.QuizScreen
import com.example.medcore.ui.theme.screens.progressScreen.ProgressScreen
import com.example.medcore.ui.theme.screens.profileScreen.ProfileScreen
import com.medcore.app.ui.screens.*


sealed class Screen(val route: String) {
    object Splash       : Screen("splash")
    object Login        : Screen("login")
    object Register     : Screen("register")
    object Home         : Screen("home")
    object SystemDetail : Screen("system/{systemId}") {
        fun createRoute(systemId: String) = "system/$systemId"
    }
    object Topic        : Screen("topic/{topicId}") {
        fun createRoute(topicId: String) = "topic/$topicId"
    }
    object Quiz         : Screen("quiz/{systemId}") {
        fun createRoute(systemId: String) = "quiz/$systemId"
    }
    object Progress     : Screen("progress")
    object Subscription : Screen("subscription")
    object Profile      : Screen("profile")
}

@Composable
fun MedCoreNavGraph(navController: NavHostController) {
    NavHost(
        navController  = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onSystemClick   = { id -> navController.navigate(Screen.SystemDetail.createRoute(id)) },
                onProgressClick = { navController.navigate(Screen.Progress.route) },
                onProfileClick  = { navController.navigate(Screen.Profile.route) },
                onSubscribeClick = { navController.navigate(Screen.Subscription.route) }
            )
        }

        composable(
            Screen.SystemDetail.route,
            arguments = listOf(navArgument("systemId") { type = NavType.StringType })
        ) { back ->
            val systemId = back.arguments?.getString("systemId") ?: ""
            SystemDetailScreen(
                systemId        = systemId,
                onBack          = { navController.popBackStack() },
                onTopicClick    = { id -> navController.navigate(Screen.Topic.createRoute(id)) },
                onQuizClick     = { navController.navigate(Screen.Quiz.createRoute(systemId)) },
                onSubscribeClick = { navController.navigate(Screen.Subscription.route) }
            )
        }

        composable(
            Screen.Topic.route,
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { back ->
            val topicId = back.arguments?.getString("topicId") ?: ""
            TopicScreen(
                topicId  = topicId,
                onBack   = { navController.popBackStack() },
                onQuizClick = { navController.navigate(Screen.Quiz.createRoute("neuro")) }
            )
        }

        composable(
            Screen.Quiz.route,
            arguments = listOf(navArgument("systemId") { type = NavType.StringType })
        ) { back ->
            val systemId = back.arguments?.getString("systemId") ?: ""
            QuizScreen(
                systemId = systemId,
                onBack   = { navController.popBackStack() }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                onBack          = { navController.popBackStack() },
                onSubscribeClick = { navController.navigate(Screen.Subscription.route) }
            )
        }

        composable(Screen.Subscription.route) {
            SubscriptionScreen(
                onBack    = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack          = { navController.popBackStack() },
                onSubscribeClick = { navController.navigate(Screen.Subscription.route) },
                onSignOut        = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}