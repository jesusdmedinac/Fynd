package com.jesusdmedinac.fynd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusdmedinac.fynd.domain.usecase.GetCurrentHostUseCase
import com.jesusdmedinac.fynd.domain.usecase.GetLeaderUseCase
import com.jesusdmedinac.fynd.domain.usecase.SetNumberOfPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val setNumberOfPlacesUseCase: SetNumberOfPlacesUseCase,
    private val getLeaderUseCase: GetLeaderUseCase,
) :
    ViewModel(),
    ContainerHost<HomeViewModel.State, HomeViewModel.SideEffect>,
    EntryBehavior {
    override val container: Container<State, SideEffect> =
        viewModelScope.container(State())

    data class State(
        val selectedTab: Int = 0,
    )

    sealed class SideEffect {
        object Idle : SideEffect()
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
}

interface EntryBehavior {
    fun onNumberClick(number: Int)
}