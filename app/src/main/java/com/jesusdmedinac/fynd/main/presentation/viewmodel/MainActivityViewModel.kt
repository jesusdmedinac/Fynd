package com.jesusdmedinac.fynd.main.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.main.domain.usecase.JoinByLeaderCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val joinByLeaderCodeUseCase: JoinByLeaderCodeUseCase
) :
    ViewModel(),
    ContainerHost<MainActivityViewModel.State, MainActivityViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    val behavior = object : Behavior {
        override fun onCodeScanned(code: String) {
            joinByLeaderCodeUseCase(code)
        }
    }

    interface Behavior {
        fun onCodeScanned(code: String)
    }

    class State

    sealed class SideEffect
}