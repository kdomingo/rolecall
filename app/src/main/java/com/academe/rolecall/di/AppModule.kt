package com.academe.rolecall.di

import android.content.Context
import androidx.room.Room
import com.academe.rolecall.data.AppDatabase
import com.academe.rolecall.data.dao.AttendanceDao
import com.academe.rolecall.data.dao.SessionDao
import com.academe.rolecall.data.dao.StudentDao
import com.academe.rolecall.data.dao.UserDao
import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.repository.AppSessionRepository
import com.academe.rolecall.data.repository.AppSessionRepositoryImpl
import com.academe.rolecall.data.repository.AttendanceRepository
import com.academe.rolecall.data.repository.AttendanceRepositoryImpl
import com.academe.rolecall.data.repository.DummyAppSessionRepositoryImpl
import com.academe.rolecall.data.repository.DummyAttendanceRepositoryImpl
import com.academe.rolecall.data.repository.DummyStudentRepositoryImpl
import com.academe.rolecall.data.repository.DummyUserRepositoryImpl
import com.academe.rolecall.data.repository.StudentRepository
import com.academe.rolecall.data.repository.StudentRepositoryImpl
import com.academe.rolecall.data.repository.UserRepository
import com.academe.rolecall.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideSessionDao(database: AppDatabase): SessionDao = database.sessionDao()

    @Provides
    fun provideStudentDao(database: AppDatabase): StudentDao = database.studentDao()

    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDao = database.attendanceDao()

    @Provides
    @Singleton
    fun provideUserRepository(
        preferences: UserPreferences,
        userDao: UserDao
    ): UserRepository {
        return runBlocking {
            if (preferences.demoModeFlow.first()) {
                DummyUserRepositoryImpl()
            } else {
                UserRepositoryImpl(userDao)
            }
        }
    }

    @Provides
    @Singleton
    fun provideStudentRepository(
        preferences: UserPreferences,
        studentDao: StudentDao
    ): StudentRepository {
        return runBlocking {
            if (preferences.demoModeFlow.first()) {
                DummyStudentRepositoryImpl()
            } else {
                StudentRepositoryImpl(studentDao)
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppSessionRepository(
        preferences: UserPreferences,
        sessionDao: SessionDao
    ): AppSessionRepository {
        return runBlocking {
            if (preferences.demoModeFlow.first()) {
                DummyAppSessionRepositoryImpl()
            } else {
                AppSessionRepositoryImpl(sessionDao)
            }
        }
    }

    @Provides
    @Singleton
    fun provideAttendanceRepository(
        preferences: UserPreferences,
        attendanceDao: AttendanceDao
    ): AttendanceRepository {
        return runBlocking {
            if (preferences.demoModeFlow.first()) {
                DummyAttendanceRepositoryImpl()
            } else {
                AttendanceRepositoryImpl(attendanceDao)
            }
        }
    }
}
