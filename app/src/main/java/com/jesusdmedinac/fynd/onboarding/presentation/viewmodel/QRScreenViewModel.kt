package com.jesusdmedinac.fynd.onboarding.presentation.viewmodel

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
    ContainerHost<QRScreenViewModel.State, QRScreenViewModel.SideEffect> {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    val behavior = object : Behavior {
        override fun generateNewCode() {
            intent {
                if (state.qrCode.isNotEmpty())
                    return@intent
                val qrCode = (0..5)
                    .toList()
                    .map { ('A'..'Z').random() }
                    .toString()
                reduce {
                    state.copy(qrCode = qrCode)
                }
            }
        }

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
    }

    interface Behavior {
        fun generateNewCode()

        fun onStartServingClick()

        fun onScanCodeClick()
    }

    data class State(
        val qrCode: String = "",
    )

    sealed class SideEffect {
        object Idle : SideEffect()
        object NavigateToPlacesScreen : SideEffect()
        object NavigateToScanCodeScreen : SideEffect()
    }
}