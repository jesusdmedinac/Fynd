package com.jesusdmedinac.fynd.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<MainScreenViewModel.State, MainScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    val behavior = object : Behavior {
        override fun goToOnboardingScreen() {
            intent {
                if (!state.isUserLoggedIn) {
                    delay(1000)
                    postSideEffect(SideEffect.NavigateToOnboardingScreen)
                }
            }
        }
    }

    interface Behavior {
        fun goToOnboardingScreen()
    }

    data class State(
        val isUserLoggedIn: Boolean = false,
    )

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToOnboardingScreen : SideEffect()
    }
}