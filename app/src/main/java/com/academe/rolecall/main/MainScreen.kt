package com.academe.rolecall.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.academe.rolecall.dashboard.DashboardScreen
import com.academe.rolecall.login.LoginScreen


@Composable
fun MainScreen() {

    val viewModel = hiltViewModel<MainViewModel>()
    val currentRoute by viewModel.currentRoute.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    if (currentRoute.isEmpty()) return

    NavHost(
        navController = navController, startDestination = currentRoute
    ) {
        composable(Screens.Login.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Dashboard.name) {
            DashboardScreen(navController = navController)
        }
    }
}