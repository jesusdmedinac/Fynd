package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Place
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesByLeaderEmailUseCase @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val currentHostUseCase: GetCurrentHostUseCase,
) {
    suspend operator fun invoke(): Result<Flow<List<Place>>> = currentHostUseCase()
        .map { it.email }
        .map { email -> placesRepository.getPlacesBy(email) }
}