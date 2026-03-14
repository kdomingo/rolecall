package com.academe.rolecall.data.service

import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.repository.StudentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentService @Inject constructor(
    private val repository: StudentRepository
) {
    suspend fun getStudents(): Result<List<Student>> {
        return try {
            val students = repository.getStudents()
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
