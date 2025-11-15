package com.zetta.tiksid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zetta.tiksid.network.SessionManager
import com.zetta.tiksid.navigation.AppNavigation
import com.zetta.tiksid.navigation.Screen
import com.zetta.tiksid.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT,
            )
        )

        val splashScreen = installSplashScreen()
        var keepSplash = true

        splashScreen.setKeepOnScreenCondition { keepSplash }

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            var startDestination by remember { mutableStateOf<Screen>(Screen.SignIn) }

            LaunchedEffect(Unit) {
                val session = sessionManager.getSession()
                startDestination = session?.let { Screen.Home } ?: Screen.SignIn

                delay(100)
                keepSplash = false

                launch {
                    if (currentDestination?.route != null && currentDestination.route == Screen.SignIn.route) return@launch
                    sessionManager.isLoggedOut.collect {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            AppTheme {
                AppNavigation(navController, startDestination)
            }
        }
    }
}