package com.jesusdmedinac.fynd.presentation.ui.navigation

sealed class NavItem(
    open val baseRoute: String,
) {
    sealed class OnboardingMainScreen(override val baseRoute: String) : NavItem(baseRoute) {
        object Host : OnboardingMainScreen("onboarding-host-screen")
        object Main : OnboardingMainScreen("onboarding-main-screen")
        object QRScreen : OnboardingMainScreen("onboarding-qr-screen")
    }

    object MainScreen : NavItem("main-screen")

    object PlacesNavItem : NavItem("places-screen")

    sealed class AuthScreen(override val baseRoute: String) : NavItem(baseRoute) {
        object Host : AuthScreen("auth-host-screen")
        object SignInScreen : AuthScreen("auth-login-screen")
        object SignUpScreen : AuthScreen("auth-signup-screen")
    }
}
