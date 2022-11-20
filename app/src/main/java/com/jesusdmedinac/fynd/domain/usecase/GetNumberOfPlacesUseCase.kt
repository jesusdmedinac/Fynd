package com.jesusdmedinac.fynd.domain.usecase

import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetNumberOfPlacesUseCase {
    suspend operator fun invoke(leaderEmail: String): Flow<Int>
}

class GetNumberOfPlacesUseCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository
) : GetNumberOfPlacesUseCase {
    override suspend fun invoke(leaderEmail: String): Flow<Int> =
        placesRepository.getNumberOfPlacesOf(leaderEmail)
}