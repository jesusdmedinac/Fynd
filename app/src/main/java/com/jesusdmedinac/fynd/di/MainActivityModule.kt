package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.main.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.main.domain.repository.LeaderRepositoryImpl
import com.jesusdmedinac.fynd.main.domain.usecase.JoinByLeaderCodeUseCase
import com.jesusdmedinac.fynd.main.domain.usecase.JoinByLeaderCodeUseCaseImpl
import com.jesusdmedinac.fynd.onboarding.domain.usecase.HostQrCodeUseCase
import com.jesusdmedinac.fynd.onboarding.domain.usecase.HostQrCodeUseCaseImpl
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
}