package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.data.remote.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsLeaderRemoteDataSource(
        leaderRemoteDataSourceImpl: LeaderRemoteDataSourceImpl
    ): LeaderRemoteDataSource

    @Binds
    fun bindsHostRemoteDataSource(
        hostRemoteDataSourceImpl: HostRemoteDataSourceImpl
    ): HostRemoteDataSource

    @Binds
    fun bindsPlacesRemoteDataSource(
        placesRemoteDataSourceImpl: PlacesRemoteDataSourceImpl
    ): PlacesRemoteDataSource
}