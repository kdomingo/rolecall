package com.academe.rolecall.data.service

import com.academe.rolecall.data.models.Student
import com.academe.rolecall.data.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentService @Inject constructor(
    private val repository: StudentRepository
) {
    fun getStudents(): Flow<List<Student>> {
        return repository.getStudents()
    }

    suspend fun deleteStudent(student: Student) {
        repository.deleteStudent(student)
    }
}
