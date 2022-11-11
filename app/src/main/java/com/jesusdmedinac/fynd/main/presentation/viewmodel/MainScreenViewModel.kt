package com.jesusdmedinac.fynd.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.main.presentation.mapper.DomainHostToUiHostMapper
import com.jesusdmedinac.fynd.main.presentation.mapper.DomainSessionToUiSessionMapper
import com.jesusdmedinac.fynd.onboarding.domain.usecase.RetrieveCurrentSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
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
    ContainerHost<MainScreenViewModel.State, MainScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    val behavior = object : Behavior {
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
                is State.Session.LoggedHost -> {
                    postSideEffect(SideEffect.NavigateToOnboardingScreen)
                    reduce {
                        state.copy(session = State.Session.LoggedHost(uiSession.host))
                    }
                }
                State.Session.LoggingIn -> {}
            }
        }

        override fun goToSignInScreen() {
            intent {
                delay(100)
                postSideEffect(SideEffect.NavigateToSignInScreen)
            }
        }
    }

    interface Behavior {
        fun goToOnboardingScreen()

        fun goToSignInScreen()
    }

    data class State(
        val session: Session = Session.LoggingIn,
    ) {
        sealed class Session {
            object LoggingIn : Session()
            object HostIsNotLoggedIn : Session()
            data class LoggedHost(
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
    }
}