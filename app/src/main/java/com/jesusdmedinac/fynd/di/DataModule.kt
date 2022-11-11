package com.jesusdmedinac.fynd.di

import com.jesusdmedinac.fynd.main.data.remote.LeaderRemoteDataSource
import com.jesusdmedinac.fynd.main.data.remote.LeaderRemoteDataSourceImpl
import com.jesusdmedinac.fynd.onboarding.data.mapper.FirebaseUserToHostUserMapper
import com.jesusdmedinac.fynd.onboarding.data.remote.HostRemoteDataSource
import com.jesusdmedinac.fynd.onboarding.data.remote.HostRemoteDataSourceImpl
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