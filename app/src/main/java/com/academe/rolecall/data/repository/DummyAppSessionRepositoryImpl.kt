package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.AppSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummyAppSessionRepositoryImpl : AppSessionRepository {
    private val sessions = mutableListOf<AppSession>()

    override suspend fun insert(session: AppSession) {
        sessions.add(session)
    }

    override suspend fun delete(session: AppSession) {
        sessions.remove(session)
    }

    override suspend fun getById(id: Long): AppSession? {
        return sessions.find { it.id == id }
    }

    override fun getAll(): Flow<List<AppSession>> {
        return flowOf(sessions.toList())
    }

    override suspend fun deleteAll() {
        sessions.clear()
    }
}
