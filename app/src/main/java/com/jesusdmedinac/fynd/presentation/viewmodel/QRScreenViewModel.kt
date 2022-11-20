package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class QRScreenViewModel @Inject constructor() :
    ViewModel(),
    QRScreenBehavior,
    ContainerHost<QRScreenViewModel.State, QRScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    override fun onStartServingClick() {
        intent {
            reduce { state.copy(isLoading = true) }
            postSideEffect(SideEffect.NavigateToPlacesScreen)
        }
    }

    override fun onScanCodeClick() {
        intent {
            reduce { state.copy(isLoading = true) }
            postSideEffect(SideEffect.NavigateToScanCodeScreen)
        }
    }

    data class State(
        val isLoading: Boolean = false,
    )

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToPlacesScreen : SideEffect()
        object NavigateToScanCodeScreen : SideEffect()
    }
}

interface QRScreenBehavior {
    fun onStartServingClick()
    fun onScanCodeClick()
}