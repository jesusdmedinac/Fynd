package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import javax.inject.Inject

class SetNumberOfPlacesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(leaderEmail: String, number: Int) {
        placesRepository.setCurrentNumberOfPlaces(leaderEmail, number)
    }
}