package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.Student

interface StudentRepository {
    suspend fun getStudents(): List<Student>
}

class StudentRepositoryImpl : StudentRepository {
    override suspend fun getStudents(): List<Student> {
        // Real implementation would fetch from DB or API
        return emptyList()
    }
}
