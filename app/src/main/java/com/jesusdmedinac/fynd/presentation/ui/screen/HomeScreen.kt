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
import com.jesusdmedinac.fynd.presentation.ui.screen.homescreen.TotalScreen
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.EntryBehavior
import com.jesusdmedinac.fynd.presentation.viewmodel.HomeScreenViewModel
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    homeState: HomeScreenViewModel.State,
    homeSideEffect: HomeScreenViewModel.SideEffect,
    entryBehavior: EntryBehavior,
) {
    val selectedTab = homeState.selectedTab
    val session = homeState.session

    entryBehavior.getCurrentSession()

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
                        selected = selectedTab == 0,
                        onClick = { entryBehavior.onTabSelected(0) },
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
                label = { Text("Zona") },
                selected = selectedTab == 1,
                onClick = { entryBehavior.onTabSelected(1) },
            )
        }
    }) { paddingValues ->
        val navController = rememberNavController()
        val startDestination = when {
            session is HomeScreenViewModel.State.Session.HostIsLoggedIn && session.host.isLeader -> NavItem.HomeNavItem.Entry.baseRoute
            else -> NavItem.HomeNavItem.Area.baseRoute
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
                AreaScreen()
            }
            composable(NavItem.HomeNavItem.Total.baseRoute) {
                TotalScreen()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun HomeScreenPreview() {
    FyndTheme {
        HomeScreen(
            homeState = HomeScreenViewModel.State(),
            homeSideEffect = HomeScreenViewModel.SideEffect.Idle,
            entryBehavior = object : EntryBehavior {
                override fun getCurrentSession() {
                    TODO("Not yet implemented")
                }

                override fun onNumberClick(number: Int) {
                    TODO("Not yet implemented")
                }

                override fun onTabSelected(tab: Int) {
                    TODO("Not yet implemented")
                }
            })
    }
}