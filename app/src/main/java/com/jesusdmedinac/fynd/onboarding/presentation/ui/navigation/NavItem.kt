package com.jesusdmedinac.fynd.onboarding.presentation.ui.navigation

sealed class NavItem(
    val baseRoute: String,
) {
    object OnboardingMainScreen : NavItem("onboarding-main-screen")
    object QRScreen : NavItem("qr-screen")
}
