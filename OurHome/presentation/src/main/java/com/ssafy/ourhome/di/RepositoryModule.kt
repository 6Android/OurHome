package com.ssafy.ourhome.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ssafy.data.datasource.family.FamilyDataSource
import com.ssafy.data.datasource.pet.PetDataSource
import com.ssafy.data.datasource.question.QuestionDataSource
import com.ssafy.data.datasource.schedule.ScheduleDataSource
import com.ssafy.data.datasource.user.UserDataSource
import com.ssafy.data.repository.pet.PetRepositoryImpl
import com.ssafy.data.repository.question.QuestionRepositoryImpl
import com.ssafy.data.repository.schedule.ScheduleRepositoryImpl
import com.ssafy.data.repository.user.UserRepositoryImpl
import com.ssafy.domain.repository.pet.PetRepository
import com.ssafy.domain.repository.question.QuestionRepository
import com.ssafy.domain.repository.schedule.ScheduleRepository
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
        fireStore: FirebaseFirestore,
        userDataSource: UserDataSource,
        familyDataSource: FamilyDataSource
    ): UserRepository {
        return UserRepositoryImpl(fireStore,userDataSource, familyDataSource)
    }

    // ScheduleRepository DI
    @Provides
    @Singleton
    fun provideScheduleRepository(
        fireStore: FirebaseFirestore,
        scheduleDataSource: ScheduleDataSource
    ): ScheduleRepository {
        return ScheduleRepositoryImpl(fireStore, scheduleDataSource)
    }

    // PetRepository DI
    @Provides
    @Singleton
    fun providePetRepository(
        petDataSource: PetDataSource
    ): PetRepository {
        return PetRepositoryImpl(petDataSource)
    }

    // QuestionRepository DI
    @Provides
    @Singleton
    fun provideQuestionRepository(
        questionDataSource: QuestionDataSource
    ): QuestionRepository {
        return QuestionRepositoryImpl(questionDataSource)
    }
}