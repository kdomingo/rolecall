package com.academe.rolecall.data.repository

import com.academe.rolecall.data.dao.UserDao
import com.academe.rolecall.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAll(): Flow<List<User>>
    suspend fun getById(id: Long): User?
    suspend fun getByEmail(email: String): User?
    suspend fun insert(user: User): Long
    suspend fun update(user: User)
    suspend fun delete(user: User)
}

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAll() = userDao.getAll()
    override suspend fun getById(id: Long) = userDao.getById(id)
    override suspend fun getByEmail(email: String) = userDao.getByEmail(email)
    override suspend fun insert(user: User) = userDao.insert(user)
    override suspend fun update(user: User) = userDao.update(user)
    override suspend fun delete(user: User) = userDao.delete(user)
}
