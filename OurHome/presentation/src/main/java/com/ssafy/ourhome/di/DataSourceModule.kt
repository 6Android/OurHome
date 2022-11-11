package com.ssafy.ourhome.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.data.datasource.family.FamilyDataSourceImpl
import com.ssafy.data.datasource.pet.PetDataSource
import com.ssafy.data.datasource.pet.PetDataSourceImpl
import com.ssafy.data.datasource.question.QuestionDataSource
import com.ssafy.data.datasource.question.QuestionDataSourceImpl
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

    // FamilyDataSource DI
    @Provides
    @Singleton
    fun provideFamilyDataSource(
        fireStore: FirebaseFirestore,
        fireAuth: FirebaseAuth
    ): FamilyDataSource {
        return FamilyDataSourceImpl(fireStore, fireAuth)
    }

    // PetDataSource DI
    @Provides
    @Singleton
    fun providePetDataSource(
        fireStore: FirebaseFirestore
    ) : PetDataSource{
        return PetDataSourceImpl(fireStore)
    }

    // QuestionDataSource DI
    @Provides
    @Singleton
    fun provideQuestionDataSource(
        fireStore: FirebaseFirestore
    ) : QuestionDataSource{
        return QuestionDataSourceImpl(fireStore)
    }
}