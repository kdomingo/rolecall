package com.academe.rolecall.data.repository

import com.academe.rolecall.data.dao.StudentDao
import com.academe.rolecall.data.models.Student
import kotlinx.coroutines.flow.first

interface StudentRepository {
    suspend fun getStudents(): List<Student>
}

class StudentRepositoryImpl(private val studentDao: StudentDao) : StudentRepository {
    override suspend fun getStudents(): List<Student> {
        return studentDao.getAll().first()
    }
}
