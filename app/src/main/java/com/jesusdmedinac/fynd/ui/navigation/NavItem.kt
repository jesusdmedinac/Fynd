package com.jesusdmedinac.fynd.ui.navigation

sealed class NavItem(
    val baseRoute: String,
) {
    object MainScreen : NavItem("main-screen")
    object PlacesNavItem : NavItem("places-screen")
}
