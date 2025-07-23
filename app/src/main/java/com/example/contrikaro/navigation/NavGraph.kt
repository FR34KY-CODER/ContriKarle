package com.example.contrikaro.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contrikaro.presentation.auth.LoginScreen
import com.example.contrikaro.presentation.group.AllGroupsScreen
import com.example.contrikaro.presentation.group.GroupDetailsScreen // Use your existing package
import com.example.contrikaro.presentation.main.MainScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.Login.route) {
            // Your LoginScreen call was here, you can restore it if needed
            // LoginScreen(navController)
        }
        composable(Routes.Main.route) {
            MainScreen(navController = navController)
        }

        // NOTE: This is the new destination for your details screen
        composable(Routes.GroupDetails.route) {
            GroupDetailsScreen(navController = navController)
        }

        // You also had a route for "groups", which we can add here
        // if AllGroupsScreen.kt is a screen you want to build.
        // composable("groups") {
        //     AllGroupsScreen(navController = navController)
        // }
    }
}