package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DummyStudentRepositoryImpl : StudentRepository {
    private val students = MutableStateFlow(
        listOf(
            Student(1, "Alice Johnson", "alice@example.com", "S001"),
            Student(2, "Bob Smith", "bob@example.com", "S002"),
            Student(3, "Charlie Davis", "charlie@example.com", "S003"),
            Student(4, "Diana Prince", "diana@example.com", "S004"),
            Student(5, "Ethan Hunt", "ethan@example.com", "S005"),
            Student(6, "Fiona Gallagher", "fiona@example.com", "S006"),
            Student(7, "George Miller", "george@example.com", "S007"),
            Student(8, "Hannah Montana", "hannah@example.com", "S008"),
            Student(9, "Ian Wright", "ian@example.com", "S009"),
            Student(10, "Julia Roberts", "julia@example.com", "S010")
        )
    )

    override fun getStudents(): Flow<List<Student>> = students

    override suspend fun deleteStudent(student: Student) {
        students.update { list -> list.filter { it.id != student.id } }
    }
}
