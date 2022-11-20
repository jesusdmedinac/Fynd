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
import com.jesusdmedinac.fynd.presentation.viewmodel.HomeViewModel

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    homeState: HomeViewModel.State,
    homeSideEffect: HomeViewModel.SideEffect,
    entryBehavior: EntryBehavior,
) {
    Scaffold(bottomBar = {
        var selectedTab = homeState.selectedTab

        NavigationBar {
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = null
                    )
                },
                label = { Text("Entrada") },
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.Square,
                        contentDescription = null
                    )
                },
                label = { Text("Zona") },
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Filled.Workspaces,
                        contentDescription = null
                    )
                },
                label = { Text("Total") },
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
            )
        }
    }) { paddingValues ->
        val navController = rememberNavController()
        NavHost(
            navController,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            startDestination = NavItem.HomeNavItem.Entry.baseRoute,
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
            homeState = HomeViewModel.State(),
            homeSideEffect = HomeViewModel.SideEffect.Idle,
            entryBehavior = object : EntryBehavior {
                override fun onNumberClick(number: Int) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}