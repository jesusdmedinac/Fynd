package com.jesusdmedinac.fynd.domain.usecase

import javax.inject.Inject

interface HostQrCodeUseCase {
    operator fun invoke(): String
}

class HostQrCodeUseCaseImpl @Inject constructor() : HostQrCodeUseCase {
    override fun invoke(): String = (0..5)
        .map { ('A'..'Z').random() }
        .joinToString("")
}