package com.ssafy.ourhome.di

import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.data.repository.user.UserRepositoryImpl
import com.ssafy.domain.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // UserRepository DI
    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}