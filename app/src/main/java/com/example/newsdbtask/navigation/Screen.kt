package com.example.newsdbtask.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Favorite : Screen("favorite_screen")
}