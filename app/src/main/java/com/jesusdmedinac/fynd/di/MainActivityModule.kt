package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.data.repository.LeaderRepositoryImpl
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.data.repository.HostRepositoryImpl
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.usecase.*
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

    @Binds
    fun bindsSignInUseCase(
        signInUseCaseImpl: SignInUseCaseImpl
    ): SignInUseCase

    @Binds
    fun bindsSignUpUseCase(
        signUpUseCaseImpl: SignUpUseCaseImpl
    ): SignUpUseCase
}