package com.academe.rolecall.data.models

import org.junit.Test
import java.lang.IllegalArgumentException

class StudentTest {

    @Test(expected = IllegalArgumentException::class)
    fun `creating student with blank name throws exception`() {
        Student(name = "", email = "test@example.com", rollNumber = "123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating student with invalid email throws exception`() {
        Student(name = "John Doe", email = "invalid-email", rollNumber = "123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `creating student with blank roll number throws exception`() {
        Student(name = "John Doe", email = "test@example.com", rollNumber = "")
    }
}
