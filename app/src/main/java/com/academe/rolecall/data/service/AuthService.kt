package com.academe.rolecall.data.service

import com.academe.rolecall.data.models.AppSession
import com.academe.rolecall.data.models.DemoAccount
import com.academe.rolecall.data.models.UserCredentials
import com.academe.rolecall.data.preferences.UserPreferences
import com.academe.rolecall.data.repository.AppSessionRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val preferences: UserPreferences,
    private val sessionRepository: AppSessionRepository
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun login(credentials: UserCredentials): Result<Unit> {
        return try {
            val demo = DemoAccount()
            val isDemo = credentials.email == demo.email && credentials.password == demo.password
            
            // Here you could add logic to verify against a database for non-demo accounts
            
            val session = AppSession(
                userId = if (isDemo) -1L else null, // Placeholder logic for demo user ID
                token = UUID.randomUUID().toString(),
                expiresOn = System.currentTimeMillis() + 3600000, // 1 hour from now
                createdOn = dateFormat.format(Date())
            )
            
            sessionRepository.insert(session)
            preferences.setDemoMode(isDemo)
            preferences.setAuthenticated(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        sessionRepository.deleteAll()
        preferences.clear()
    }
}
