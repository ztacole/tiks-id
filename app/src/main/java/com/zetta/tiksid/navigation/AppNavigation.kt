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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.zetta.tiksid.R
import com.zetta.tiksid.ui.components.BackButton
import com.zetta.tiksid.ui.components.BottomNavBar
import com.zetta.tiksid.ui.screen.auth.signin.SignIn
import com.zetta.tiksid.ui.screen.auth.signup.SignUp
import com.zetta.tiksid.ui.screen.movie.browse.Browse
import com.zetta.tiksid.ui.screen.movie.detail.MovieDetail
import com.zetta.tiksid.ui.screen.movie.detail.MovieDetailViewModel
import com.zetta.tiksid.ui.screen.movie.home.Home
import com.zetta.tiksid.ui.screen.ticket.detail.TicketDetail
import com.zetta.tiksid.ui.screen.ticket.detail.TicketDetailViewModel
import com.zetta.tiksid.ui.screen.ticket.list.TicketList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val topAppBarScreens = listOf(
        Screen.MovieDetail.route,
        Screen.TicketDetail.route
    )
    val showTopAppBar = currentDestination?.route in topAppBarScreens
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val fraction = scrollBehavior.state.collapsedFraction

    Scaffold(
        topBar = {
            if (showTopAppBar) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f - fraction),
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f - fraction),
                        scrolledContainerColor = Color.Transparent
                    ),
                    title = {
                        Text(
                            text = when (currentDestination?.route) {
                                Screen.TicketDetail.route -> stringResource(R.string.ticket_detail_text_topbar)
                                else -> ""
                            },
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                        )
                    },
                    navigationIcon = {
                        BackButton(
                            onClick = { navController.navigateUp() },
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f - fraction)
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    onNavigate = {
                        if (currentDestination.route != it) {
                            navController.navigate(it) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    currentDestination = currentDestination!!
                )
            }
        }
    ) { innerPadding ->
        val paddingModifier =
            if (currentDestination?.route != Screen.MovieDetail.route) innerPadding.calculateTopPadding()
            else PaddingValues(0.dp).calculateTopPadding()

        val bottomNavScreenEnterTransition = fadeIn(tween(200, easing = EaseIn))
        val bottomNavScreenExitTransition = fadeOut(tween(200, easing = EaseIn))

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingModifier),
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
                    navArgument("movieId", { type = NavType.IntType })
                )
            ) {
                val viewModel: MovieDetailViewModel = koinViewModel()
                MovieDetail(
                    viewModel = viewModel,
                    onNavigateToTicketList = { navController.navigate(Screen.TicketList.route) }
                )
            }
            composable(
                route = Screen.TicketDetail.route,
                arguments = listOf(
                    navArgument("ticketId", { type = NavType.IntType })
                )
            ) {
                val viewModel: TicketDetailViewModel  = koinViewModel()
                TicketDetail(
                    viewModel = viewModel,
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                )
            }
        }
    }
}