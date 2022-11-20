package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.domain.usecase.RetrieveCurrentSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val joinByLeaderCodeUseCase: JoinByLeaderCodeUseCase,
    private val retrieveCurrentSessionUseCase: RetrieveCurrentSessionUseCase,
) :
    ViewModel(),
    MainActivityBehavior,
    ContainerHost<MainActivityViewModel.State, MainActivityViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun retrieveCurrentSession() {
        intent {
            retrieveCurrentSessionUseCase(viewModelScope)
        }
    }

    override fun onCodeScanned(code: String) {
        intent {
            runCatching { joinByLeaderCodeUseCase(code) }
                .onSuccess {
                    delay(100)
                    postSideEffect(SideEffect.NavigateToHome)
                }
                .onFailure { println(it) }
        }
    }

    class State

    sealed class SideEffect {
        object NavigateToHome : SideEffect()
        object Idle : SideEffect()
    }
}

interface MainActivityBehavior {
    fun retrieveCurrentSession()
    fun onCodeScanned(code: String)
}