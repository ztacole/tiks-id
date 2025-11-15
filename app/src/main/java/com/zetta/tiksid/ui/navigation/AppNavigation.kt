package com.zetta.tiksid.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingModifier)
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
            composable(Screen.Home.route) {
                Home(
                    onNavigateToMovieDetail = { navController.navigate(Screen.MovieDetail.createRoute(it)) }
                )
            }
            composable(Screen.Browse.route) {
                Browse(
                    onNavigateToMovieDetail = { navController.navigate(Screen.MovieDetail.createRoute(it)) }
                )
            }
            composable(Screen.TicketList.route) {
                TicketList(
                    onNavigateToTicketDetail = { navController.navigate(Screen.TicketDetail.createRoute(it)) }
                )
            }
            composable(Screen.MovieDetail.route) {
                val movieId = it.arguments?.getString("movieId")
                if (movieId != null) {
                    MovieDetail(
                        movieId = movieId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToTicketList = { navController.navigate(Screen.TicketList.route) }
                    )
                }
            }
            composable(Screen.TicketDetail.route) {
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