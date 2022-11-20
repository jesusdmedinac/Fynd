package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import javax.inject.Inject

interface SetNumberOfPlacesUseCase {
    suspend operator fun invoke(leaderEmail: String, number: Int)
}

class SetNumberOfPlacesUseCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository
) : SetNumberOfPlacesUseCase {
    override suspend fun invoke(leaderEmail: String, number: Int) {
        placesRepository.setCurrentNumberOfPlacesUseCase(leaderEmail, number)
    }
}