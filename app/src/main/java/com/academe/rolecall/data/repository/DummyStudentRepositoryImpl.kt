package com.academe.rolecall.data.repository

import com.academe.rolecall.data.models.Student
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
class DummyStudentRepositoryImpl constructor() : StudentRepository {
    override suspend fun getStudents(): List<Student> {
        delay(1000) // Simulate network delay
        return listOf(
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
    }
}
