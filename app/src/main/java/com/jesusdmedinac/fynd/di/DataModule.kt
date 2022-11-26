package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.data.remote.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindsLeaderRemoteDataSource(
        leaderRemoteDataSourceImpl: LeaderRemoteDataSourceImpl
    ): LeaderRemoteDataSource

    @Binds
    @Singleton
    fun bindsHostRemoteDataSource(
        hostRemoteDataSourceImpl: HostRemoteDataSourceImpl
    ): HostRemoteDataSource

    @Binds
    @Singleton
    fun bindsPlacesRemoteDataSource(
        placesRemoteDataSourceImpl: PlacesRemoteDataSourceImpl
    ): PlacesRemoteDataSource
}