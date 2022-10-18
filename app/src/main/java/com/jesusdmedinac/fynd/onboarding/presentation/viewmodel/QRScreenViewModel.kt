package com.jesusdmedinac.fynd.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
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
    }

    interface Behavior {
        fun generateNewCode()
    }

    data class State(
        val qrCode: String = "",
    )

    sealed class SideEffect {
        object Idle : SideEffect()
    }
}