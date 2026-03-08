package com.academe.rolecall.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.academe.rolecall.login.LoginScreen


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Screens.Login.name
    ) {
        composable(Screens.Login.name) {
            LoginScreen()
        }
    }
}