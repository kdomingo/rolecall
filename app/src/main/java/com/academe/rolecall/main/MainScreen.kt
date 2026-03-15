package com.academe.rolecall.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.academe.rolecall.dashboard.DashboardScreen
import com.academe.rolecall.login.LoginScreen


@Composable
fun MainScreen() {

    val viewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Screens.Splash.name
    ) {
        composable(Screens.Splash.name) {
            SplashScreen(navController = navController)
        }
        composable(Screens.Login.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Dashboard.name) {
            DashboardScreen(navController = navController)
        }
    }
}