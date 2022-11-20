package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.data.remote.HostRemoteDataSource
import com.jesusdmedinac.fynd.data.remote.HostRemoteDataSourceImpl
import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.data.remote.LeaderRemoteDataSourceImpl
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
}