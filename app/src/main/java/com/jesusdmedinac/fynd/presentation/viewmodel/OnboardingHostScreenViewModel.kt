package com.jesusdmedinac.fynd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.domain.usecase.IsHostALeaderUseCase
import com.jesusdmedinac.fynd.domain.usecase.SetOnboardingWelcomeScreenViewedUseCase
import com.jesusdmedinac.fynd.presentation.mapper.DomainHostToOnboardingMainScreenStateHostMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class OnboardingHostScreenViewModel @Inject constructor(
    private val getCurrentHostUseCase: GetCurrentHostUseCase,
    private val domainHostToOnboardingMainScreenStateHostMapper: DomainHostToOnboardingMainScreenStateHostMapper,
    private val setOnboardingWelcomeScreenViewedUseCase: SetOnboardingWelcomeScreenViewedUseCase,
    private val isHostALeaderUseCase: IsHostALeaderUseCase,
) :
    ViewModel(),
    OnboardingMainScreenBehavior,
    ContainerHost<OnboardingHostScreenViewModel.State, OnboardingHostScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun onScreenLoad() {
        intent {
            runCatching { getCurrentHostUseCase() }
                .onFailure { Log.e("dani", it.message.toString()) }
                .onSuccess { host ->
                    val stateHost = domainHostToOnboardingMainScreenStateHostMapper.map(host)
                    onSessionStateChange(State.Session.HostIsLoggedIn(stateHost))
                }
        }
    }

    override fun onStartClick() {
        intent {
            setOnboardingWelcomeScreenViewedUseCase(true)
            postSideEffect(SideEffect.NavigateToQRScreen)
        }
    }

    private suspend fun SimpleSyntax<State, SideEffect>.onSessionStateChange(uiSession: State.Session) {
        postSideEffect(SideEffect.Idle)
        return when (uiSession) {
            State.Session.HostIsNotLoggedIn -> {
                reduce { state.copy(session = State.Session.HostIsNotLoggedIn) }
            }
            is State.Session.HostIsLoggedIn -> {
                val host = uiSession.host
                Log.e("dani", "${isHostALeaderUseCase(host.email)}")
                if (isHostALeaderUseCase(host.email)) {
                    postSideEffect(SideEffect.NavigateToPlacesScreen)
                }
                reduce {
                    state.copy(session = State.Session.HostIsLoggedIn(uiSession.host))
                }
            }
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
            val email: String,
            val displayName: String,
            val qrCode: String,
            val isLeader: Boolean,
            val isOnboardingWelcomeScreenViewed: Boolean,
        )
    }

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToQRScreen : SideEffect()
        object NavigateToPlacesScreen : SideEffect()
    }
}

interface OnboardingMainScreenBehavior {
    fun onScreenLoad()
    fun onStartClick()
}