package com.jesusdmedinac.fynd.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
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

        override fun onSignInResult(firebaseAuthUIAuthenticationResult: FirebaseAuthUIAuthenticationResult) {
            println("dani ${firebaseAuthUIAuthenticationResult.idpResponse?.error}")
        }
    }

    interface Behavior {
        fun onCodeScanned(code: String)
        fun onSignInResult(firebaseAuthUIAuthenticationResult: FirebaseAuthUIAuthenticationResult)
    }

    class State

    sealed class SideEffect
}