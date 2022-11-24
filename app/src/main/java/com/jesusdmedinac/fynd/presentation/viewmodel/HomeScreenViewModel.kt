package com.jesusdmedinac.fynd.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.domain.usecase.GetLeaderUseCase
import com.jesusdmedinac.fynd.domain.usecase.GetNumberOfPlacesUseCase
import com.jesusdmedinac.fynd.domain.usecase.SetNumberOfPlacesUseCase
import com.jesusdmedinac.fynd.presentation.mapper.DomainHostToHomeScreenStateHostMapper
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
class HomeScreenViewModel @Inject constructor(
    private val setNumberOfPlacesUseCase: SetNumberOfPlacesUseCase,
    private val getLeaderUseCase: GetLeaderUseCase,
    private val getCurrentHostUseCase: GetCurrentHostUseCase,
    private val domainHostToHomeScreenStateHostMapper: DomainHostToHomeScreenStateHostMapper,
    private val getNumberOfPlacesUseCase: GetNumberOfPlacesUseCase,
) :
    ViewModel(),
    ContainerHost<HomeScreenViewModel.State, HomeScreenViewModel.SideEffect>,
    EntryBehavior {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun onScreenLoad() {
        intent {
            runCatching { getCurrentHostUseCase() }
                .onFailure { Log.e("dani", it.message.toString()) }
                .onSuccess { host ->
                    val stateHost = domainHostToHomeScreenStateHostMapper.map(host)
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
                reduce {
                    state.copy(session = State.Session.HostIsLoggedIn(uiSession.host))
                }
            }
        }
    }

    override fun onNumberClick(number: Int) {
        intent {
            runCatching { getLeaderUseCase() }
                .onSuccess { leader ->
                    val email = leader.email
                    setNumberOfPlacesUseCase(email, number)
                }
                .onFailure { println(it) }
        }
    }

    override fun onTabSelected(selectedTab: State.HomeTabs) {
        intent {
            reduce { state.copy(selectedTab = selectedTab) }
            val sideEffect = when (selectedTab) {
                State.HomeTabs.AreaTab -> SideEffect.NavigateToAreaScreen
                State.HomeTabs.EntryTab -> SideEffect.NavigateToEntryScreen
                State.HomeTabs.TotalTab -> SideEffect.NavigateToTotalScreen
            }
            postSideEffect(sideEffect)
        }
    }

    override fun retrieveNextPlacesNumber() {
        intent {
            runCatching { getLeaderUseCase() }
                .onSuccess { leader ->
                    val email = leader.email
                    getNumberOfPlacesUseCase(email)
                        .collect { numberOfPlaces ->
                            reduce { state.copy(numberOfPlaces = numberOfPlaces) }
                        }
                }
                .onFailure { println(it) }
        }
    }

    data class State(
        val numberOfPlaces: Int = 0,
        val selectedTab: HomeTabs = HomeTabs.EntryTab,
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

        sealed class HomeTabs {
            object EntryTab : HomeTabs()
            object AreaTab : HomeTabs()
            object TotalTab : HomeTabs()
        }
    }

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToEntryScreen : SideEffect()
        object NavigateToAreaScreen : SideEffect()
        object NavigateToTotalScreen : SideEffect()
    }
}

interface EntryBehavior {
    fun onScreenLoad()
    fun onNumberClick(number: Int)
    fun onTabSelected(selectedTab: HomeScreenViewModel.State.HomeTabs)
    fun retrieveNextPlacesNumber()
}