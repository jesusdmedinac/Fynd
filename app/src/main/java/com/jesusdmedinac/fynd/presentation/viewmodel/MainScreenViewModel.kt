package com.jesusdmedinac.fynd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.presentation.mapper.DomainHostToStateHostMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val getCurrentHostUseCase: GetCurrentHostUseCase,
    private val domainHostToStateHostMapper: DomainHostToStateHostMapper,
) :
    ViewModel(),
    MainScreenBehavior,
    ContainerHost<MainScreenViewModel.State, MainScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun getCurrentSession() {
        intent {
            runCatching { getCurrentHostUseCase() }
                .onFailure { Log.e("dani", it.message.toString()) }
                .onSuccess { host ->
                    val stateHost = domainHostToStateHostMapper.map(host)
                    onSessionStateChange(State.Session.HostIsLoggedIn(stateHost))
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
        intent {
            delay(100)
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
            val email: String,
            val displayName: String,
            val qrCode: String,
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
    fun getCurrentSession()
    fun goToSignInScreen()
    fun goToSignUpScreen()
}