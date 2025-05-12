package com.example.newsdbtask.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsdbtask.R
import com.example.newsdbtask.navigation.nav_bar.BottomNavItem
import com.example.newsdbtask.navigation.nav_bar.BottomNavigationBar
import com.example.newsdbtask.ui.presentation.chart.ChartScreen
import com.example.newsdbtask.ui.presentation.favorite.FavoriteScreen
import com.example.newsdbtask.ui.presentation.home.HomeScreen
import com.example.newsdbtask.ui.presentation.more.MoreScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
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
        ),
        BottomNavItem(
            label = "Chart",
            icon = R.drawable.chart_ic
        ),
        BottomNavItem(
            label = "More",
            icon = R.drawable.more_ic
        )
    )

    val selectedItemIndex = when {
        currentRoute?.contains(Screen.Home.route) == true -> 0
        currentRoute?.contains(Screen.Favorite.route) == true -> 1
        currentRoute?.contains(Screen.Chart.route) == true -> 2
        currentRoute?.contains(Screen.More.route) == true -> 3
        else -> -1
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            if (
                currentRoute?.contains(Screen.Home.route) == true ||
                currentRoute?.contains(Screen.Favorite.route) == true ||
                currentRoute?.contains(Screen.Chart.route) == true ||
                currentRoute?.contains(Screen.More.route) == true
            ) {
                BottomNavigationBar(
                    items = bottomNavItems,
                    selectedIndex = selectedItemIndex,
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

                            2 -> {
                                navController.navigate(Screen.Chart.route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            3 -> {
                                navController.navigate(Screen.More.route) {
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
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                if (targetState.destination.route == Screen.More.route) {
                    fadeIn() + scaleIn()
                } else EnterTransition.None
            },
            exitTransition = {
                if (initialState.destination.route == Screen.More.route) {
                    fadeOut() + scaleOut()

                } else ExitTransition.None
            },
            popEnterTransition = {
                if (targetState.destination.route == Screen.More.route) {
                    fadeIn() + scaleIn()
                } else EnterTransition.None
            },
            popExitTransition = {
                if (initialState.destination.route == Screen.More.route) {
                    fadeOut() + scaleOut()
                } else ExitTransition.None
            }

        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(Modifier.fillMaxSize())
            }
            composable(Screen.Chart.route) {
                ChartScreen(Modifier.fillMaxSize())
            }
            composable(Screen.More.route) {
                MoreScreen(Modifier.fillMaxSize())
            }
        }

    }
}