package com.zetta.tiksid.navigation

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.zetta.tiksid.ui.components.BottomNavBar
import com.zetta.tiksid.ui.screen.auth.signin.SignIn
import com.zetta.tiksid.ui.screen.auth.signup.SignUp
import com.zetta.tiksid.ui.screen.movie.browse.Browse
import com.zetta.tiksid.ui.screen.movie.detail.MovieDetail
import com.zetta.tiksid.ui.screen.movie.home.Home
import com.zetta.tiksid.ui.screen.ticket.detail.TicketDetail
import com.zetta.tiksid.ui.screen.ticket.list.TicketList

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Screen
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavScreens = listOf(
        Screen.Home.route,
        Screen.Browse.route,
        Screen.TicketList.route
    )
    val showBottomNav = currentDestination?.route in bottomNavScreens

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    onNavigate = { navController.navigate(it) },
                    currentDestination = currentDestination!!
                )
            }
        }
    ) { innerPadding ->
        val paddingModifier =
            if (currentDestination?.route != Screen.MovieDetail.route) innerPadding
            else PaddingValues(0.dp)

        val bottomNavScreenEnterTransition = fadeIn(tween(200, easing = EaseIn))
        val bottomNavScreenExitTransition = fadeOut(tween(200, easing = EaseIn))

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingModifier),
            enterTransition = {
                slideInHorizontally(animationSpec = tween(350, delayMillis = 50, easing = EaseOut), initialOffsetX = { it })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = EaseOut))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400, easing = EaseOut))
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec = tween(350, delayMillis = 50, easing = EaseOut), targetOffsetX = { it })
            }
        ) {
            composable(Screen.SignIn.route) {
                SignIn(
                    onNavigateToHome = { navController.navigate(Screen.Home.route) },
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
                )
            }
            composable(Screen.SignUp.route) {
                SignUp(
                    onNavigateToSignIn = { navController.navigate(Screen.SignIn.route) }
                )
            }
            composable(
                route = Screen.Home.route,
                enterTransition = { bottomNavScreenEnterTransition },
                exitTransition = { bottomNavScreenExitTransition }
            ) {
                Home(
                    onNavigateToMovieDetail = { navController.navigate(Screen.MovieDetail.createRoute(it)) }
                )
            }
            composable(
                route = Screen.Browse.route,
                enterTransition = { bottomNavScreenEnterTransition },
                exitTransition = { bottomNavScreenExitTransition }
            ) {
                Browse(
                    onNavigateToMovieDetail = { navController.navigate(Screen.MovieDetail.createRoute(it)) }
                )
            }
            composable(
                route = Screen.TicketList.route,
                enterTransition = { bottomNavScreenEnterTransition },
                exitTransition = { bottomNavScreenExitTransition }
            ) {
                TicketList(
                    onNavigateToTicketDetail = { navController.navigate(Screen.TicketDetail.createRoute(it)) }
                )
            }
            composable(
                route = Screen.MovieDetail.route,
                arguments = listOf(
                    navArgument("movieId", builder = {
                        type = NavType.StringType
                    })
                )
            ) {
                val movieId = it.arguments?.getString("movieId")
                if (movieId != null) {
                    MovieDetail(
                        movieId = movieId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToTicketList = { navController.navigate(Screen.TicketList.route) }
                    )
                }
            }
            composable(
                route = Screen.TicketDetail.route,
                arguments = listOf(
                    navArgument("ticketId", builder = {
                        type = NavType.StringType
                    })
                )
            ) {
                val ticketId = it.arguments?.getString("ticketId")
                if (ticketId != null) {
                    TicketDetail(
                        ticketId = ticketId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}