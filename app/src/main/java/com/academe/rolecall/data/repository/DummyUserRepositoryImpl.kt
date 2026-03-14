package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class DummyUserRepositoryImpl : UserRepository {
    private val users = MutableStateFlow<List<User>>(emptyList())

    override fun getAll(): Flow<List<User>> = users

    override suspend fun getById(id: Long): User? = users.value.find { it.id == id }

    override suspend fun getByEmail(email: String): User? = users.value.find { it.email == email }

    override suspend fun insert(user: User): Long {
        val newId = (users.value.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
        val newUser = user.copy(id = newId)
        users.value = users.value + newUser
        return newId
    }

    override suspend fun update(user: User) {
        users.value = users.value.map { if (it.id == user.id) user else it }
    }

    override suspend fun delete(user: User) {
        users.value = users.value.filter { it.id != user.id }
    }
}
