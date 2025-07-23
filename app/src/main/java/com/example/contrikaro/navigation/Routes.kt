package com.example.contrikaro.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login_screen")
    object Main : Routes("main_screen")
    object AllGroups : Routes("all_groups_screen") // Added this route
    object GroupDetails : Routes("group_details/{groupId}") {
        fun createRoute(groupId: String) = "group_details/$groupId"
    }
}