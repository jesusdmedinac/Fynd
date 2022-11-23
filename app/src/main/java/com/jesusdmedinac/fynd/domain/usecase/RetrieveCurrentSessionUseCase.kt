package com.jesusdmedinac.fynd.domain.usecase

import android.util.Log
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.presentation.viewmodel.MainScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class RetrieveCurrentSessionUseCase @Inject constructor(
    private val hostRepository: HostRepository,
    private val getCurrentHostUseCase: GetCurrentHostUseCase,
) {
    suspend operator fun invoke(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            while (true) {
                delay(Random.nextInt(1, 10) * 1000L)
                runCatching { getCurrentHostUseCase() }
                    .onFailure { Log.e("dani", it.message.toString()) }
                    .onSuccess { host ->
                        hostRepository.retrieveCurrentSession(host.email)
                    }
            }
        }
    }
}