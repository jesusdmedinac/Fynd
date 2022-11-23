package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.data.repository.HostRepositoryImpl
import com.jesusdmedinac.fynd.data.repository.LeaderRepositoryImpl
import com.jesusdmedinac.fynd.data.repository.PlacesRepositoryImpl
import com.jesusdmedinac.fynd.data.repository.UserSettingsRepositoryImpl
import com.jesusdmedinac.fynd.domain.repository.HostRepository
import com.jesusdmedinac.fynd.domain.repository.LeaderRepository
import com.jesusdmedinac.fynd.domain.repository.PlacesRepository
import com.jesusdmedinac.fynd.domain.repository.UserSettingsRepository
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
    fun bindsHostRepository(
        hostRepositoryImpl: HostRepositoryImpl,
    ): HostRepository

    @Binds
    fun bindsPlacesRepository(
        placesRepositoryImpl: PlacesRepositoryImpl
    ): PlacesRepository

    @Binds
    fun bindsUserSettingsRepository(
        userSettingsRepositoryImpl: UserSettingsRepositoryImpl
    ): UserSettingsRepository
}