package com.jesusdmedinac.fynd.di

import android.app.Application
import androidx.room.Room
import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jesusdmedinac.fynd.data.local.FyndDataBase
import com.jesusdmedinac.fynd.data.local.HostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun providesFyndDataBase(application: Application): FyndDataBase = Room
        .databaseBuilder(application, FyndDataBase::class.java, "fynd-database")
        .build()

    @Provides
    fun providesHostDao(fyndDataBase: FyndDataBase): HostDao = fyndDataBase.hostDao()

    @Provides
    @Named("sha256")
    fun providesHashFunction(): HashFunction = Hashing.sha256()
}