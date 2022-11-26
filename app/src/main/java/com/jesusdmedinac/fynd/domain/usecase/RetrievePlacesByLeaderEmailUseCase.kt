package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.model.Place
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrievePlacesByLeaderEmailUseCase @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val currentHostUseCase: GetCurrentHostUseCase,
) {
    suspend operator fun invoke(): Result<Unit> = currentHostUseCase()
        .map { it.email }
        .map { email -> placesRepository.retrievePlacesBy(email) }
}