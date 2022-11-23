package com.jesusdmedinac.fynd.presentation.ui.navigation

sealed class NavItem(
    open val baseRoute: String,
) {
    sealed class OnboardingHostScreen(override val baseRoute: String) : NavItem(baseRoute) {
        object Host : OnboardingHostScreen("onboarding-host-screen")
        object Main : OnboardingHostScreen("onboarding-main-screen")
        object QRScreen : OnboardingHostScreen("onboarding-qr-screen")
    }

    object MainScreen : NavItem("main-screen")

    object PlacesNavItem : NavItem("places-screen")

    sealed class HomeNavItem(override val baseRoute: String) : NavItem(baseRoute) {
        object Host : HomeNavItem("home-host-screen")
        object Entry : HomeNavItem("home-entry-screen")
        object Area : HomeNavItem("home-area-screen")
        object Total : HomeNavItem("home-total-screen")
    }

    sealed class AuthScreen(override val baseRoute: String) : NavItem(baseRoute) {
        object Host : AuthScreen("auth-host-screen")
        object SignInScreen : AuthScreen("auth-login-screen")
        object SignUpScreen : AuthScreen("auth-signup-screen")
    }
}
