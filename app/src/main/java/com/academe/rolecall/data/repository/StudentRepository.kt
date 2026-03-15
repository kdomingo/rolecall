package com.academe.rolecall.data.repository

import com.academe.rolecall.data.dao.StudentDao
import com.academe.rolecall.data.models.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getStudents(): Flow<List<Student>>
    suspend fun deleteStudent(student: Student)
}

class StudentRepositoryImpl(private val studentDao: StudentDao) : StudentRepository {
    override fun getStudents(): Flow<List<Student>> {
        return studentDao.getAll()
    }

    override suspend fun deleteStudent(student: Student) {
        studentDao.delete(student)
    }
}
