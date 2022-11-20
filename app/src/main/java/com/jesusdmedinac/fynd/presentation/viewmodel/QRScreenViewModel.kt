package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
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
            postSideEffect(SideEffect.NavigateToPlacesScreen)
        }
    }

    override fun onScanCodeClick() {
        intent {
            postSideEffect(SideEffect.NavigateToScanCodeScreen)
        }
    }

    class State
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