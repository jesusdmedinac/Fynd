package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.main.data.remote.LeaderRepositoryImpl
import com.jesusdmedinac.fynd.main.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.main.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.main.domain.usecase.JoinByLeaderCodeUseCaseImpl
import com.jesusdmedinac.fynd.onboarding.data.remote.HostRepositoryImpl
import com.jesusdmedinac.fynd.onboarding.domain.repository.HostRepository
import com.jesusdmedinac.fynd.onboarding.domain.usecase.HostQrCodeUseCase
import com.jesusdmedinac.fynd.onboarding.domain.usecase.HostQrCodeUseCaseImpl
import com.jesusdmedinac.fynd.onboarding.domain.usecase.RetrieveCurrentSessionUseCase
import com.jesusdmedinac.fynd.onboarding.domain.usecase.RetrieveCurrentSessionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface MainActivityModule {
    @Binds
    fun bindsLeaderRepository(
        leaderRepositoryImpl: LeaderRepositoryImpl
    ): LeaderRepository

    @Binds
    fun bindsJoinByLeaderCodeUseCase(
        joinByLeaderCodeUseCaseImpl: JoinByLeaderCodeUseCaseImpl
    ): JoinByLeaderCodeUseCase

    @Binds
    fun bindsHostQrCodeUseCase(
        hostQrCodeUseCaseImpl: HostQrCodeUseCaseImpl,
    ): HostQrCodeUseCase

    @Binds
    fun bindsHostRepository(
        hostRepositoryImpl: HostRepositoryImpl,
    ): HostRepository

    @Binds
    fun bindsRetrieveCurrentSessionUseCase(
        retrieveCurrentSessionUseCaseImpl: RetrieveCurrentSessionUseCaseImpl
    ): RetrieveCurrentSessionUseCase
}