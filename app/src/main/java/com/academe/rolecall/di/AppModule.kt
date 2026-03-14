package com.academe.rolecall.di

import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.repository.DummyStudentRepositoryImpl
import com.academe.rolecall.data.repository.StudentRepository
import com.academe.rolecall.data.repository.StudentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStudentRepository(
        preferences: UserPreferences
    ): StudentRepository {
        return runBlocking {
            if (preferences.demoModeFlow.first()) {
                DummyStudentRepositoryImpl()
            } else {
                StudentRepositoryImpl()
            }
        }
    }
}
