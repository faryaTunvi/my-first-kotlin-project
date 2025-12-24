package com.example.mymurmurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymurmurapp.di.AppContainer
import com.example.mymurmurapp.presentation.auth.AuthViewModel
import com.example.mymurmurapp.presentation.auth.SignInScreen
import com.example.mymurmurapp.presentation.auth.SignUpScreen
import com.example.mymurmurapp.presentation.navigation.Screen
import com.example.mymurmurapp.presentation.post.PostMurmurScreen
import com.example.mymurmurapp.presentation.profile.UserProfileScreen
import com.example.mymurmurapp.presentation.profile.UserProfileViewModel
import com.example.mymurmurapp.presentation.timeline.TimelineScreen
import com.example.mymurmurapp.presentation.timeline.TimelineViewModel
import com.example.mymurmurapp.ui.theme.MyMurmurAppTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMurmurAppTheme {
                MurmurApp()
            }
        }
    }
}

@Composable
fun MurmurApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    val startDestination = if (authUiState.isAuthenticated) {
        Screen.Timeline.route
    } else {
        Screen.SignIn.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        // Auth screens
        composable(
            route = Screen.SignIn.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            SignInScreen(
                viewModel = authViewModel,
                onSignInSuccess = {
                    navController.navigate(Screen.Timeline.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(
            route = Screen.SignUp.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            SignUpScreen(
                viewModel = authViewModel,
                onSignUpSuccess = {
                    navController.navigate(Screen.Timeline.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.popBackStack()
                }
            )
        }

        // Timeline screen
        composable(
            route = Screen.Timeline.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            val timelineViewModel: TimelineViewModel = viewModel()
            TimelineScreen(
                viewModel = timelineViewModel,
                onNavigateToProfile = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                },
                onNavigateToPostMurmur = {
                    navController.navigate(Screen.PostMurmur.route)
                },
                onNavigateToOwnProfile = {
                    runBlocking {
                        val currentUserId = AppContainer.provideAuthRepository().getCurrentUserId()
                        currentUserId?.let {
                            navController.navigate(Screen.UserProfile.createRoute(it))
                        }
                    }
                }
            )
        }

        // Post Murmur screen
        composable(
            route = Screen.PostMurmur.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) + expandVertically() },
            exitTransition = { fadeOut(animationSpec = tween(300)) + shrinkVertically() }
        ) {
            val timelineViewModel: TimelineViewModel = viewModel(
                viewModelStoreOwner = navController.getBackStackEntry(Screen.Timeline.route)
            )
            PostMurmurScreen(
                viewModel = timelineViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // User Profile screen
        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            val profileViewModel: UserProfileViewModel = viewModel()

            UserProfileScreen(
                userId = userId,
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToUser = { newUserId ->
                    navController.navigate(Screen.UserProfile.createRoute(newUserId))
                }
            )
        }
    }
}

