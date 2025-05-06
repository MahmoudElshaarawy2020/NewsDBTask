package com.example.newsdbtask.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsdbtask.R
import com.example.newsdbtask.navigation.nav_bar.BottomNavItem
import com.example.newsdbtask.navigation.nav_bar.BottomNavigationBar
import com.example.newsdbtask.ui.presentation.favorite.FavoriteScreen
import com.example.newsdbtask.ui.presentation.home.HomeScreen
import com.example.newsdbtask.ui.presentation.home.HomeViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    println("Current route: $currentRoute")

    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = R.drawable.home_ic
        ),
        BottomNavItem(
            label = "Bookmark",
            icon = R.drawable.wishlist_ic
        )
    )

    val selectedItemIndex = when {
        currentRoute?.contains(Screen.Home.route) == true -> 0
        currentRoute?.contains(Screen.Favorite.route) == true -> 1
        else -> -1
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute?.contains(Screen.Home.route) == true || currentRoute?.contains(Screen.Favorite.route) == true) {
                BottomNavigationBar(
                    items = bottomNavItems,
                    selectedItem = selectedItemIndex,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            1 -> {
                                navController.navigate(Screen.Favorite.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable("home_screen") {
                HomeScreen(
                    navController = navController,
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                )
            }
        }
    }
}