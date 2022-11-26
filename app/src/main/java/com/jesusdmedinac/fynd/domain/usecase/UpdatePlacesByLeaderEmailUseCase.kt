package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Place
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import javax.inject.Inject

class UpdatePlacesByLeaderEmailUseCase @Inject constructor(
    private val currentHostUseCase: GetCurrentHostUseCase,
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(places: List<Place>): Result<Unit> = currentHostUseCase()
        .map { it.email }
        .map { email -> placesRepository.updatePlacesBy(email, places) }
}