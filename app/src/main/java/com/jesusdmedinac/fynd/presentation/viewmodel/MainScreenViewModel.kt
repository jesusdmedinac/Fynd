package com.jesusdmedinac.fynd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.presentation.mapper.DomainSessionToUiSessionMapper
import com.jesusdmedinac.fynd.domain.usecase.RetrieveCurrentSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val retrieveCurrentSessionUseCase: RetrieveCurrentSessionUseCase,
    private val domainSessionToUiSessionMapper: DomainSessionToUiSessionMapper,
) :
    ViewModel(),
    MainScreenBehavior,
    ContainerHost<MainScreenViewModel.State, MainScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun goToOnboardingScreen() {
        intent {
            retrieveCurrentSessionUseCase()
                .map { domainSession ->
                    domainSessionToUiSessionMapper.map(domainSession)
                }
                .distinctUntilChanged()
                .collectLatest { uiSession ->
                    onSessionStateChange(uiSession)
                }
        }
    }

    private suspend fun SimpleSyntax<State, SideEffect>.onSessionStateChange(uiSession: State.Session) {
        when (uiSession) {
            State.Session.HostIsNotLoggedIn -> {
                postSideEffect(SideEffect.Idle)
                reduce { state.copy(session = State.Session.HostIsNotLoggedIn) }
            }
            is State.Session.HostIsLoggedIn -> {
                postSideEffect(SideEffect.NavigateToOnboardingScreen)
                reduce {
                    state.copy(session = State.Session.HostIsLoggedIn(uiSession.host))
                }
            }
        }
    }

    override fun goToSignInScreen() {
        intent {
            delay(100)
            postSideEffect(SideEffect.NavigateToSignInScreen)
        }
    }

    override fun goToSignUpScreen() {
        Log.d("dani", "-1 goToSignUpScreen")
        intent {
            delay(100)
            Log.d("dani", "0 SideEffect.NavigateToSignUpScreen")
            postSideEffect(SideEffect.NavigateToSignUpScreen)
        }
    }

    data class State(
        val session: Session = Session.HostIsNotLoggedIn,
    ) {
        sealed class Session {
            object HostIsNotLoggedIn : Session()
            data class HostIsLoggedIn(
                val host: Host,
            ) : Session()
        }

        data class Host(
            val username: String,
            val isLeader: Boolean,
        )
    }

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToOnboardingScreen : SideEffect()
        object NavigateToSignInScreen : SideEffect()
        object NavigateToSignUpScreen : SideEffect()
    }
}

interface MainScreenBehavior {
    fun goToOnboardingScreen()
    fun goToSignInScreen()
    fun goToSignUpScreen()
}