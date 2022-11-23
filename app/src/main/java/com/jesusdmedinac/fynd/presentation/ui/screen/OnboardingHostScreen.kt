package com.jesusdmedinac.fynd.presentation.ui.screen

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusdmedinac.fynd.domain.model.*
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.domain.repository.UserSettingsRepository
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.domain.usecase.IsHostALeaderUseCase
import com.jesusdmedinac.fynd.domain.usecase.SetOnboardingWelcomeScreenViewedUseCase
import com.jesusdmedinac.fynd.presentation.mapper.DomainHostToOnboardingMainScreenStateHostMapper
import com.jesusdmedinac.fynd.presentation.ui.navigation.NavItem
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import com.jesusdmedinac.fynd.presentation.viewmodel.*
import kotlinx.coroutines.flow.Flow

@ExperimentalMaterial3Api
@Composable
fun OnboardingHostScreen(
    onboardingHostScreenViewModel: OnboardingHostScreenViewModel,
    qrScreenViewModel: QRScreenViewModel,
    onNavigateToPlacesScreenClick: () -> Unit,
    onNavigateToScanCodeScreenClick: () -> Unit,
) {
    val onboardingMainScreenState by onboardingHostScreenViewModel.container.stateFlow.collectAsState()
    val onboardingMainScreenSideEffect by onboardingHostScreenViewModel.container.sideEffectFlow.collectAsState(
        initial = OnboardingHostScreenViewModel.SideEffect.Idle
    )

    LaunchedEffect(Unit) {
        onboardingHostScreenViewModel.onScreenLoad()
    }

    val navController = rememberNavController()
    val session = onboardingMainScreenState.session
    val startDestination = when {
        session is OnboardingHostScreenViewModel.State.Session.HostIsLoggedIn
                && session.host.isOnboardingWelcomeScreenViewed -> {
            NavItem.OnboardingHostScreen.QRScreen
        }
        else -> NavItem.OnboardingHostScreen.Main
    }

    LaunchedEffect(onboardingMainScreenSideEffect) {
        when (onboardingMainScreenSideEffect) {
            OnboardingHostScreenViewModel.SideEffect.NavigateToQRScreen -> {
                navController.navigate(NavItem.OnboardingHostScreen.QRScreen.baseRoute)
            }
            OnboardingHostScreenViewModel.SideEffect.NavigateToPlacesScreen -> {
                onNavigateToPlacesScreenClick()
            }
            OnboardingHostScreenViewModel.SideEffect.Idle -> Unit
        }
    }

    NavHost(
        navController = navController, startDestination = startDestination.baseRoute
    ) {
        composable(NavItem.OnboardingHostScreen.Main.baseRoute) {
            OnboardingMainScreen(
                session,
                onboardingHostScreenViewModel,
            )
        }

        composable(NavItem.OnboardingHostScreen.QRScreen.baseRoute) {
            val qrScreenState by qrScreenViewModel.container.stateFlow.collectAsState()
            val qrScreenSideEffect by qrScreenViewModel.container.sideEffectFlow.collectAsState(
                initial = QRScreenViewModel.SideEffect.Idle
            )

            LaunchedEffect(qrScreenSideEffect) {
                when (qrScreenSideEffect) {
                    QRScreenViewModel.SideEffect.Idle -> Unit
                    QRScreenViewModel.SideEffect.NavigateToPlacesScreen -> onNavigateToPlacesScreenClick()
                    QRScreenViewModel.SideEffect.NavigateToScanCodeScreen -> onNavigateToScanCodeScreenClick()
                }
            }

            QRScreen(
                session,
                qrScreenState,
                qrScreenViewModel,
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun OnboardingScreenPreview() {
    FyndTheme {
        val hostRepository = object : HostRepository {
            override suspend fun retrieveCurrentSession(email: String) {
                TODO("Not yet implemented")
            }

            override suspend fun getCurrentSession(): Flow<Session> {
                TODO("Not yet implemented")
            }

            override suspend fun getCurrentHost(): Host? {
                TODO("Not yet implemented")
            }

            override suspend fun signIn(signInUserCredentials: SignInUserCredentials): SignInResult {
                TODO("Not yet implemented")
            }

            override suspend fun signUp(signUpUserCredentials: SignUpUserCredentials): SignUpResult {
                TODO("Not yet implemented")
            }
        }
        OnboardingHostScreen(
            onboardingHostScreenViewModel = OnboardingHostScreenViewModel(
                getCurrentHostUseCase = GetCurrentHostUseCase(
                    hostRepository = hostRepository
                ),
                domainHostToOnboardingMainScreenStateHostMapper = DomainHostToOnboardingMainScreenStateHostMapper(),
                SetOnboardingWelcomeScreenViewedUseCase(userSettingsRepository = object :
                    UserSettingsRepository {
                    override suspend fun isOnboardingWelcomeScreenViewed(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override suspend fun setIsOnboardingWelcomeScreenViewed(
                        isOnboardingWelcomeScreenViewed: Boolean
                    ) {
                        TODO("Not yet implemented")
                    }
                }),
                isHostALeaderUseCase = IsHostALeaderUseCase(leaderRepository = object :
                    LeaderRepository {
                    override suspend fun joinBy(leaderCode: String, hostCode: String) {
                        TODO("Not yet implemented")
                    }

                    override suspend fun getCurrentLeader(): Host? {
                        TODO("Not yet implemented")
                    }

                    override suspend fun isLeader(email: String): Boolean {
                        TODO("Not yet implemented")
                    }
                })
            ),
            qrScreenViewModel = QRScreenViewModel(),
            onNavigateToPlacesScreenClick = {},
            onNavigateToScanCodeScreenClick = {},
        )
    }
}