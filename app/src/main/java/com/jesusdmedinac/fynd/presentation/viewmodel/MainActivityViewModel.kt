package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.jesusdmedinac.fynd.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.domain.usecase.RetrieveCurrentSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
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
            retrieveCurrentSessionUseCase()
        }
    }

    override fun onCodeScanned(code: String) {
        intent {
            joinByLeaderCodeUseCase(code)
        }
    }

    class State

    sealed class SideEffect
}

interface MainActivityBehavior {
    fun retrieveCurrentSession()
    fun onCodeScanned(code: String)
}