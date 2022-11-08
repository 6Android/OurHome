package com.ssafy.ourhome.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.data.datasource.user.UserDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    // UserDataSource DI
    @Provides
    @Singleton
    fun provideUserDataSource(
        fireStore: FirebaseFirestore,
        fireAuth: FirebaseAuth
    ): UserDataSource {
        return UserDataSourceImpl(fireStore, fireAuth)
    }
}