package com.example.newsdbtask.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsdbtask.R
import com.example.newsdbtask.navigation.Screen.Favorite.route
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
    var previousRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentRoute) {
        if (currentRoute != previousRoute) {
            // Set isNavigated flag when changing between Home and Favorite
            val isNavigatingToHome =
                currentRoute == Screen.Home.route && previousRoute == Screen.Favorite.route
            val isNavigatingFromHome =
                previousRoute == Screen.Home.route && currentRoute == Screen.Favorite.route

            if (isNavigatingToHome || isNavigatingFromHome) {
                viewModel.setIsNavigated(true)
            }
            previousRoute = currentRoute
        }
    }

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
        else -> -1 // No selection for other screens
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Show bottom navigation only on Home and UserProfile screens
            if (currentRoute?.contains(Screen.Home.route) == true || currentRoute?.contains(Screen.Favorite.route) == true) {
                BottomNavigationBar(
                    items = bottomNavItems,
                    selectedItem = selectedItemIndex,
                    onItemSelected = { index ->
                        when (index) {
                            0 -> {
                                viewModel.prepareForNavigation()
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            1 -> {
                                viewModel.prepareForNavigation()
                                navController.navigate(Screen.Favorite.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                        // Navigate to the selected route
//                        navController.navigate(route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                inclusive = false
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
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
                .padding(innerPadding)
        ) {
            // Home Screen
            composable("home_screen") {
                HomeScreen(
                    navController = navController,
                )
            }

            // UserProfile Screen
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}