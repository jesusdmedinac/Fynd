package com.jesusdmedinac.fynd.main.presentation.ui.navigation

sealed class NavItem(
    val baseRoute: String,
) {
    object MainScreen : NavItem("main-screen")
    object PlacesNavItem : NavItem("places-screen")
    object ScanCodeNavItem : NavItem("scan-code-screen")
}
