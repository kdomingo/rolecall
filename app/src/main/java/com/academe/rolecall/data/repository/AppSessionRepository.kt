package com.academe.rolecall.data.repository

import com.academe.rolecall.data.dao.SessionDao
import com.academe.rolecall.data.models.AppSession
import kotlinx.coroutines.flow.Flow

interface AppSessionRepository {
    suspend fun insert(session: AppSession)
    suspend fun delete(session: AppSession)
    suspend fun getById(id: Long): AppSession?
    fun getAll(): Flow<List<AppSession>>
    suspend fun deleteAll()
}

class AppSessionRepositoryImpl(private val sessionDao: SessionDao) : AppSessionRepository {
    override suspend fun insert(session: AppSession) = sessionDao.insert(session)
    override suspend fun delete(session: AppSession) = sessionDao.delete(session)
    override suspend fun getById(id: Long) = sessionDao.getById(id)
    override fun getAll() = sessionDao.getAll()
    override suspend fun deleteAll() = sessionDao.deleteAll()
}
