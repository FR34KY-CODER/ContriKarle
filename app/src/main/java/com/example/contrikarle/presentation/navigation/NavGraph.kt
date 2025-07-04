package com.example.contrikarle.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contrikarle.presentation.screens.dashboard.DashboardScreen
import com.example.contrikarle.presentation.screens.login.LoginScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { /* navigate to home */ },
                //webClientId = "Y152296767175-hqrljgqvg6lmm4u8foctaunr6b93e9qo.apps.googleusercontent.com"
            )
        }
        composable("dashboard") {
            DashboardScreen() }
    }
}