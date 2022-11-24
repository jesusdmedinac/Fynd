package com.jesusdmedinac.fynd.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.screen.homescreen.AreaScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.homescreen.EntryScreen
import com.jesusdmedinac.fynd.presentation.ui.screen.homescreen.PlacesScreen
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.EntryBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.HomeScreenViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.PlacesScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun HomeHostScreen(
    homeState: HomeScreenViewModel.State,
    homeSideEffect: HomeScreenViewModel.SideEffect,
    entryBehavior: EntryBehavior,
    placesScreenViewModel: PlacesScreenViewModel,
) {
    val numberOfPlaces = homeState.numberOfPlaces
    val selectedTab = homeState.selectedTab
    val session = homeState.session

    LaunchedEffect(Unit) {
        entryBehavior.onScreenLoad()
    }

    Scaffold(bottomBar = {
        NavigationBar {
            when {
                session is HomeScreenViewModel.State.Session.HostIsLoggedIn && session.host.isLeader -> {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Filled.Home, contentDescription = null
                            )
                        },
                        label = { Text("Entrada") },
                        selected = selectedTab == HomeScreenViewModel.State.HomeTabs.EntryTab,
                        onClick = { entryBehavior.onTabSelected(HomeScreenViewModel.State.HomeTabs.EntryTab) },
                    )
                }
                else -> Unit
            }
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.Square, contentDescription = null
                    )
                },
                label = { Text("Area") },
                selected = selectedTab == HomeScreenViewModel.State.HomeTabs.AreaTab,
                onClick = { entryBehavior.onTabSelected(HomeScreenViewModel.State.HomeTabs.AreaTab) },
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.BlurOn, contentDescription = null
                    )
                },
                label = { Text("Zona") },
                selected = selectedTab == HomeScreenViewModel.State.HomeTabs.TotalTab,
                onClick = { entryBehavior.onTabSelected(HomeScreenViewModel.State.HomeTabs.TotalTab) },
            )
        }
    }) { paddingValues ->
        val navController = rememberNavController()
        val startDestination = when {
            session is HomeScreenViewModel.State.Session.HostIsLoggedIn
                    && session.host.isLeader -> NavItem.HomeNavItem.Entry.baseRoute
            else -> NavItem.HomeNavItem.Area.baseRoute
        }

        LaunchedEffect(homeSideEffect) {
            when (homeSideEffect) {
                HomeScreenViewModel.SideEffect.NavigateToEntryScreen -> {
                    navController.navigate(NavItem.HomeNavItem.Entry.baseRoute)
                }
                HomeScreenViewModel.SideEffect.NavigateToAreaScreen -> {
                    navController.navigate(NavItem.HomeNavItem.Area.baseRoute)
                }
                HomeScreenViewModel.SideEffect.NavigateToTotalScreen -> {
                    navController.navigate(NavItem.HomeNavItem.Total.baseRoute)
                }
                HomeScreenViewModel.SideEffect.Idle -> Unit
            }
        }

        NavHost(
            navController,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            startDestination = startDestination,
        ) {
            composable(NavItem.HomeNavItem.Entry.baseRoute) {
                EntryScreen(
                    onNumberClick = entryBehavior::onNumberClick
                )
            }

            composable(NavItem.HomeNavItem.Area.baseRoute) {
                entryBehavior.retrieveNextPlacesNumber()
                AreaScreen(
                    numberOfPlaces = numberOfPlaces
                )
            }

            composable(NavItem.HomeNavItem.Total.baseRoute) {
                val placesScreenState by placesScreenViewModel.container.stateFlow.collectAsState()
                val placesScreenSideEffect by placesScreenViewModel
                    .container
                    .sideEffectFlow
                    .collectAsState(initial = PlacesScreenViewModel.SideEffect.Idle)

                PlacesScreen(
                    placesScreenState,
                    placesScreenSideEffect,
                    placesScreenViewModel,
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun HomeScreenPreview() {
    FyndTheme {
        HomeHostScreen(
            homeState = HomeScreenViewModel.State(),
            homeSideEffect = HomeScreenViewModel.SideEffect.Idle,
            entryBehavior = object : EntryBehavior {
                override fun onScreenLoad() {
                    TODO("Not yet implemented")
                }

                override fun onNumberClick(number: Int) {
                    TODO("Not yet implemented")
                }

                override fun onTabSelected(selectedTab: HomeScreenViewModel.State.HomeTabs) {
                    TODO("Not yet implemented")
                }

                override fun retrieveNextPlacesNumber() {
                    TODO("Not yet implemented")
                }
            },
            placesScreenViewModel = PlacesScreenViewModel()
        )
    }
}